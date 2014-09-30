package org.kaiyi.wishlist.pojo;

import org.kaiyi.wishlist.database.DBRow;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kaiyi on 9/7/14.
 */
public abstract class WishItem implements DBRow, Serializable{

    protected Date gmtCreate;

    protected int type = TYPE.SHOP;
    protected int id;

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    static public class TYPE {
        public final static int SHOP = 1;
    }
}
