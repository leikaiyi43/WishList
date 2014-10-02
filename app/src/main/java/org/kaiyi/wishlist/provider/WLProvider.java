package org.kaiyi.wishlist.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.utils.Constant;

/**
 * Created by kaiyi on 10/2/14.
 */
public class WLProvider extends ContentProvider {


    private static final int ITEM_LIST = 1;
    private static final int ITEM_ID = 2;

    private static final UriMatcher sUriMatcher;
    private WishListDbHelper mHelper = null;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(WLItems.AUTHORITY, "items", ITEM_LIST);
        sUriMatcher.addURI(WLItems.AUTHORITY, "items/#", ITEM_ID);
    }


    @Override
    public boolean onCreate() {

        mHelper = WishListDbHelper.getInstance(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        String limit = null;

        switch (sUriMatcher.match(uri)) {
            case ITEM_LIST:
                builder.setTables(WishListDbHelper.WishListEntry.TABLE_NAME);
                builder.appendWhere(WishListDbHelper.WishListEntry.COLUMN_IS_DELETED + " = 0");

                // create limit
//                int size = Integer.valueOf(uri.getQueryParameter(Constant.KEY.SIZE));
//                int page = Integer.valueOf(uri.getQueryParameter(Constant.KEY.PAGE));
//                limit = (page * size) + ", " + size;

                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WLItems.SORT_ORDER_DEFAULT;
                }
                break;
            case ITEM_ID:
                builder.setTables(WishListDbHelper.WishListEntry.TABLE_NAME);
                builder.appendWhere(WishListDbHelper.WishListEntry.COLUMN_IS_DELETED + " = 0");
                builder.appendWhere(WishListDbHelper.WishListEntry._ID + " = " + uri.getLastPathSegment());
                break;
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder, limit);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case ITEM_LIST:
                return WLItems.CONTENT_TYPE;
            case ITEM_ID:
                return WLItems.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri result = null;

        switch (sUriMatcher.match(uri)) {
            case ITEM_LIST:
                long id = db.insert(WishListDbHelper.WishListEntry.TABLE_NAME, null, values);
                result = getUriForId(id, uri);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = 0;

        switch (sUriMatcher.match(uri)) {
            case ITEM_ID:
                rows = db.delete(WishListDbHelper.WishListEntry.TABLE_NAME,
                        WishListDbHelper.WishListEntry._ID + " = " + uri.getLastPathSegment(),
                        null);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = 0;

        switch (sUriMatcher.match(uri)) {
            case ITEM_ID:
                rows = db.update(WishListDbHelper.WishListEntry.TABLE_NAME,
                        values,
                        WishListDbHelper.WishListEntry._ID + " = " + uri.getLastPathSegment(),
                        null);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    public static class WLItems {
        public static final String AUTHORITY = "org.kaiyi.wishlist.provider";

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.org.kaiyi.provider.items";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.org.kaiyi.provider.items";

        public static final String SORT_ORDER_DEFAULT = "_ID DESC";
    }

    private Uri getUriForId(long id, Uri uri) {
        Uri itemUri = null;
        if (id > 0) {
            itemUri = ContentUris.withAppendedId(uri, id);
        }
        return itemUri;
    }

}
