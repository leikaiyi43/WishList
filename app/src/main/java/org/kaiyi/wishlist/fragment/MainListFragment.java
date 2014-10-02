package org.kaiyi.wishlist.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.kaiyi.wishlist.R;
import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.pojo.ShopWishItem;
import org.kaiyi.wishlist.utils.Constant;

/**
 * Created by kaiyi on 9/7/14.
 */
public class MainListFragment extends ListFragment implements EditShopDialog.EditShopDialogEvent, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainListFragment.class.getName();

    protected WishListAdapter mAdapter;
    protected WishListDbHelper mDbHelper;
    protected Boolean isCompleted;
    protected Spinner mOperateSpinner;
    protected EditShopDialog mEditShopDialog;

    private int mfilter;
//    private int size = 5;
//    private int page = 0;

    private ItemsObserver mItemsObserver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDbHelper = WishListDbHelper.getInstance(getActivity());


        mAdapter = new WishListAdapter(getActivity(), null);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(Constant.LOADER_ID.LOAD_ITEMS_ALL, null, this);

    }

    @Override
    public void onItemChanged(ShopWishItem oldItem, ShopWishItem newItem) {
    }

    @Override
    public void onItemDelete(ShopWishItem oldItem) {
    }

    @Override
    public void onCancel() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {

        Uri uri = Uri.parse(Constant.URI.ITEMS);
        Uri.Builder builder = uri.buildUpon();
        uri = builder.build();

        Log.i(TAG, uri.toString());

        switch (loaderID) {
            case Constant.LOADER_ID.LOAD_ITEMS_ALL:
                return new CursorLoader(
                        getActivity(),
                        uri,
                        WishListDbHelper.WishListEntry.SHOP_ITEM_PROJECTION,
                        null,
                        null,
                        null
                );
            case Constant.LOADER_ID.LOAD_ITEMS_COMPLETED:
                return new CursorLoader(
                        getActivity(),
                        uri,
                        WishListDbHelper.WishListEntry.SHOP_ITEM_PROJECTION,
                        "is_completed = 1",
                        null,
                        null
                );
            case Constant.LOADER_ID.LOAD_ITEMS_NOT_COMPLETED:
                return new CursorLoader(
                        getActivity(),
                        uri,
                        WishListDbHelper.WishListEntry.SHOP_ITEM_PROJECTION,
                        "is_completed = 0",
                        null,
                        null
                );

            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    class WishListAdapter extends CursorAdapter {

        public WishListAdapter(Context context, Cursor c) {
            super(context, c, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_list_item_1, parent, false);

            ViewHolder holder = new ViewHolder();

            holder.rootView = view.findViewById(R.id.root);
            holder.contentView = (TextView) view.findViewById(R.id.content);
            holder.priceView = (TextView) view.findViewById(R.id.price);

            view.setTag(holder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_CONTENT));
            float price = cursor.getFloat(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_PRICE));
            boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(WishListDbHelper.WishListEntry.COLUMN_IS_COMPLETED)) == 1;

            ViewHolder holder = (ViewHolder) view.getTag();

            holder.contentView.setText(content);
            holder.priceView.setText(String.format("%.2f元", price));
            if (isCompleted) {
                holder.rootView.setBackgroundResource(R.color.completed);
            } else {
                holder.rootView.setBackgroundResource(R.color.white);
            }

        }

        class ViewHolder {
            public View rootView;
            public TextView contentView;
            public TextView priceView;
        }


    }

    public void loadList() {
        loadList(mfilter);
    }

    public void loadList(int filter) {
        mfilter = filter;
        LoaderManager manager = getLoaderManager();
        int loaderId = 0;

        switch (filter) {
            case Constant.FILTER.NON:
                loaderId = Constant.LOADER_ID.LOAD_ITEMS_ALL;
                break;
            case Constant.FILTER.COMPLETED:
                loaderId = Constant.LOADER_ID.LOAD_ITEMS_COMPLETED;
                break;
            case Constant.FILTER.NOT_COMPLETED:
                loaderId = Constant.LOADER_ID.LOAD_ITEMS_NOT_COMPLETED;
                break;
        }

        if (manager.getLoader(loaderId) != null) {
            manager.restartLoader(loaderId, null, this);
        } else {
            manager.initLoader(loaderId, null, this);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        ShopWishItem item = new ShopWishItem((Cursor) getListAdapter().getItem(position));

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY.WISH_ITEM, item);

        EditShopDialog dialog = new EditShopDialog();
        dialog.setArguments(bundle);
        dialog.setEditShopDialogEvent(this);

        dialog.show(getFragmentManager(), MainListFragment.class.getName());

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onResume() {
        super.onResume();

        mItemsObserver = new ItemsObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(
                Uri.parse(Constant.URI.ITEMS),
                true,
                mItemsObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(mItemsObserver);
    }

    private class ItemsObserver extends ContentObserver {

        public ItemsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            loadList();
        }
    }
}
