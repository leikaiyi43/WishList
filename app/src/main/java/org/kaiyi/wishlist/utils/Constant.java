package org.kaiyi.wishlist.utils;

/**
 * Created by kaiyi on 9/13/14.
 */
public class Constant {
    public static class KEY {
        public static final String WISH_ITEM = "wish_item";
        public static final String SIZE = "size";
        public static final String PAGE = "page";
    }

    public static class LOADER_ID {
        public static final int LOAD_ITEMS_ALL = 1;
        public static final int LOAD_ITEMS_COMPLETED = 2;
        public static final int LOAD_ITEMS_NOT_COMPLETED = 3;
    }

    public static class URI {
        public static final String ITEMS = "content://org.kaiyi.wishlist.provider/items";
    }

    public static class FILTER {
        public static final int NON = 1;
        public static final int COMPLETED = 2;
        public static final int NOT_COMPLETED = 3;
    }
}
