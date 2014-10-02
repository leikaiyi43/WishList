package org.kaiyi.wishlist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.kaiyi.wishlist.pojo.ShopWishItem;

/**
 * Created by kaiyi on 9/9/14.
 */
public class WishDbDao {

    public static int completed(WishListDbHelper helper, int id, boolean completed) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WishListDbHelper.WishListEntry.COLUMN_IS_COMPLETED, completed);

        int rows = db.update(WishListDbHelper.WishListEntry.TABLE_NAME, values, WishListDbHelper.WishListEntry._ID + " = ?",
                new String[]{String.valueOf(id)});

        return rows;
    }

    public static Cursor queryList (WishListDbHelper helper, Boolean completed) {

        String selection = WishListDbHelper.WishListEntry.COLUMN_IS_DELETED + " != 1 ";
        String[] selectionArgs = null;
        if (completed != null) {
            selection += " AND " + WishListDbHelper.WishListEntry.COLUMN_IS_COMPLETED + " = ? ";
            selectionArgs = new String[] {completed ? "1" : "0"};
        }



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(WishListDbHelper.WishListEntry.TABLE_NAME, WishListDbHelper.WishListEntry.SHOP_ITEM_PROJECTION,
                selection, selectionArgs,
                null, null, null);

        return cursor;
    }

    public static int delete(WishListDbHelper helper, int id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WishListDbHelper.WishListEntry.COLUMN_IS_DELETED, true);
        int rows = db.update(WishListDbHelper.WishListEntry.TABLE_NAME, values,
                WishListDbHelper.WishListEntry._ID + " = ? ", new String[] {String.valueOf(id)});
        return rows;
    }

    public static int updateShopItem(WishListDbHelper helper, ShopWishItem item) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = item.getValues();
        int rows = db.update(WishListDbHelper.WishListEntry.TABLE_NAME, values, WishListDbHelper.WishListEntry._ID + " = ? ",
            new String[] {String.valueOf(item.getId())});

        return rows;
    }
}
