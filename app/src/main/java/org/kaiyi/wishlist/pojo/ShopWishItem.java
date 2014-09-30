package org.kaiyi.wishlist.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.kaiyi.wishlist.R;
import org.kaiyi.wishlist.database.WishListDbHelper;

import java.io.Serializable;

/**
 * Created by kaiyi on 9/7/14.
 */
public class ShopWishItem extends WishItem implements Serializable {

    protected String content;
    protected float price;
    protected boolean completed;
    protected boolean deleted;

    public ShopWishItem(String content, float price) {
        this.content = content;
        this.price = price;
        this.completed = false;
        this.deleted = false;
    }

    public ShopWishItem(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry._ID));
        content = cursor.getString(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_CONTENT));
        price = cursor.getFloat(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_PRICE));
        completed = cursor.getShort(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_IS_COMPLETED)) > 0;
        deleted = cursor.getShort(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_IS_DELETED)) > 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
//        values.put(WishListDbHelper.WishListEntry._ID, id);
        values.put(WishListDbHelper.WishListEntry.COLUMN_CONTENT, content);
        values.put(WishListDbHelper.WishListEntry.COLUMN_PRICE, price);
        values.put(WishListDbHelper.WishListEntry.COLUMN_IS_COMPLETED, completed);

        return values;
    }

    public void setId(int id) {
        this.id = id;
    }


}
