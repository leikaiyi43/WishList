package org.kaiyi.wishlist.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import org.kaiyi.wishlist.R;
import org.kaiyi.wishlist.database.WishDbDao;
import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.pojo.ShopWishItem;
import org.kaiyi.wishlist.utils.Constant;

/**
 * Created by kaiyi on 9/13/14.
 */
public class EditShopDialog extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected EditText mContent;
    protected EditText mPrice;
    protected Button mDelete;
    protected Button mCancel;
    protected Button mComfirm;
    protected Switch mComplete;
    protected WishListDbHelper mHelper;
    protected ShopWishItem mItem;
    protected boolean mIsCompleted = false;
    protected EditShopDialogEvent mEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItem = (ShopWishItem) getArguments().getSerializable(Constant.KEY.WISH_ITEM);

        mHelper = WishListDbHelper.getInstance(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_shop_dialog, null);

        mDelete = (Button) view.findViewById(R.id.btn_delete);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
        mComfirm = (Button) view.findViewById(R.id.btn_comfirm);
        mComplete = (Switch) view.findViewById(R.id.switch_completed);
        mContent = (EditText) view.findViewById(R.id.content_edit);
        mPrice = (EditText) view.findViewById(R.id.price_edit);

        mDelete.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mComfirm.setOnClickListener(this);
        mComplete.setOnCheckedChangeListener(this);

        if (mItem != null) {
            mContent.setText(mItem.getContent());
            mPrice.setText(String.valueOf(mItem.getPrice()));
            mComplete.setChecked(mItem.isCompleted());
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();

        return dialog;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_delete) {

            // send delete request to provider
            Uri uri = ContentUris.withAppendedId(Uri.parse(Constant.URI.ITEMS), mItem.getId());
            getActivity().getContentResolver().delete(uri, null, null);

            if (mEvent != null) {
                mEvent.onItemDelete(mItem);
            }

            dismiss();

        } else if (id == R.id.btn_cancel) {
            getDialog().dismiss();

            if (mEvent != null) {
                mEvent.onCancel();
            }

        } else if (id == R.id.btn_comfirm) {
            String content = mContent.getText().toString();
            float price = Float.valueOf(mPrice.getText().toString());

            ShopWishItem item = new ShopWishItem(content, price);
            item.setCompleted(mIsCompleted);
            item.setId(mItem.getId());

            Uri uri = ContentUris.withAppendedId(Uri.parse(Constant.URI.ITEMS), mItem.getId());
            getActivity().getContentResolver().update(uri, item.getValues(), null, null);

            if (mEvent != null) {
                mEvent.onItemChanged(mItem, item);
            }

            dismiss();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mIsCompleted = isChecked;
    }

    public void setEditShopDialogEvent(EditShopDialogEvent event) {
        this.mEvent = event;
    }

    public static interface EditShopDialogEvent {
        public void onItemChanged(ShopWishItem oldItem, ShopWishItem newItem);

        public void onItemDelete(ShopWishItem oldItem);

        public void onCancel();
    }
}
