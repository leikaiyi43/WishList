package org.kaiyi.wishlist.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.kaiyi.wishlist.R;
import org.kaiyi.wishlist.database.WishDbDao;
import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.pojo.ShopWishItem;
import org.kaiyi.wishlist.utils.Constant;

/**
 * Created by kaiyi on 9/7/14.
 */
public class MainListFragment extends ListFragment implements EditShopDialog.EditShopDialogEvent {

    private static final String TAG = MainListFragment.class.getName();

    protected WishListAdapter mAdapter;
    protected WishListDbHelper mDbHelper;
    protected Boolean isCompleted;
    protected Spinner mOperateSpinner;
    protected EditShopDialog mEditShopDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDbHelper = WishListDbHelper.getInstance(getActivity());
        mAdapter = new WishListAdapter(getActivity(), WishDbDao.queryList(mDbHelper, null));
        setListAdapter(mAdapter);
    }

    @Override
    public void onItemChanged(ShopWishItem oldItem, ShopWishItem newItem) {
        dataChanged();
    }

    @Override
    public void onItemDelete(ShopWishItem oldItem) {
        dataChanged();
    }

    @Override
    public void onCancel() {

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

    public void dataChanged() {
        mAdapter.changeCursor(WishDbDao.queryList(mDbHelper, isCompleted));
        mAdapter.notifyDataSetChanged();
    }

    public void dataChanged(Boolean completed) {
        isCompleted = completed;
        mAdapter.changeCursor(WishDbDao.queryList(mDbHelper, completed));
        mAdapter.notifyDataSetChanged();
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
}
