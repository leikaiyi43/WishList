package org.kaiyi.wishlist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by kaiyi on 9/7/14.
 */
public class WishListDbHelper extends SQLiteOpenHelper {

    private static WishListDbHelper helper;

    public static WishListDbHelper getInstance(Context context) {
        if (helper == null) {
            helper = new WishListDbHelper(context);
        }
        return helper;
    }

    private WishListDbHelper(Context context) {
        super(context, WishListEntry.DATABASE_NAME, null, WishListEntry.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WishListEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WishListEntry.SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    public static class WishListEntry implements BaseColumns {

        public static final String DATABASE_NAME = "wish_list.db";
        public static final int DATABASE_VERSION = 3;

        public static final String TABLE_NAME = "it_shop";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_GMT_CREATE = "gmt_create";
        public static final String COLUMN_IS_COMPLETED = "is_completed";
        public static final String COLUMN_IS_DELETED = "is_deleted";

        public static final String TEXT = " TEXT";
        public static final String VARCHAR_32 = " VARCHAR(32)";
        public static final String VARCHAR_64 = " VARCHAR(64)";
        public static final String VARCHAR_128 = " VARCHAR(128)";
        public static final String VARCHAR_512 = " VARCHAR(512)";
        public static final String VARCHAR_1K = " VARCHAR(1024)";
        public static final String DECIMAL_10D2 = " DECIMAL(10, 2)";
        public static final String TINYINT_1 = " TINYINT(1)";
        public static final String DATETIME = " DATETIME";
        public static final String COMMA_SEP = " ,";
        public static final String DEFAULT = " DEFAULT ";


        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_CONTENT + VARCHAR_1K + COMMA_SEP +
                COLUMN_PRICE + DECIMAL_10D2 + COMMA_SEP +
                COLUMN_IS_COMPLETED + TINYINT_1 + DEFAULT + '0' + COMMA_SEP +
                COLUMN_IS_DELETED + TINYINT_1 + DEFAULT + '0' + COMMA_SEP +
                COLUMN_GMT_CREATE + DATETIME + ")";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String[] SHOP_ITEM_PROJECTION = {
                _ID,
                COLUMN_CONTENT,
                COLUMN_PRICE,
                COLUMN_IS_COMPLETED,
                COLUMN_IS_DELETED
        };
    }
}
