package org.kaiyi.wishlist.database;

import android.content.ContentValues;

/**
 * Created by kaiyi on 9/7/14.
 */
public interface DBRow {
    public ContentValues getValues();

    public int getId();

}
