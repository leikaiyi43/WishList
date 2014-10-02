package org.kaiyi.wishlist.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.kaiyi.wishlist.R;
import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.pojo.ShopWishItem;
import org.kaiyi.wishlist.pojo.WishItem;


/**
 * Created by kaiyi on 9/7/14.
 */
public class AddShopWishDialogFragment extends DialogFragment implements TextWatcher, DialogInterface.OnShowListener {

    private EditText contentEdit;
    private EditText priceEdit;
    private Button posButton;
    private AddWishDialogListener mListener;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        rootView = inflater.inflate(R.layout.add_dialog, null);
        contentEdit = (EditText) rootView.findViewById(R.id.content_edit);
        priceEdit =  (EditText) rootView.findViewById(R.id.price_edit);

        contentEdit.addTextChangedListener(this);
        priceEdit.addTextChangedListener(this);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(rootView);
        builder.setPositiveButton("确定", new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                ShopWishItem item = new ShopWishItem(contentEdit.getText().toString()
                        , Float.valueOf(priceEdit.getText().toString()));
                mListener.onDialogPositiveClick(AddShopWishDialogFragment.this, item);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(AddShopWishDialogFragment.this);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(this);
        posButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        return alertDialog;
    }

    public AddWishDialogListener getListener() {
        return mListener;
    }

    public void setListener(AddWishDialogListener listener) {
        this.mListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(contentEdit.getText().toString())
                || TextUtils.isEmpty(priceEdit.getText().toString())) {
            posButton.setEnabled(false);
        } else {
            posButton.setEnabled(true);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        posButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setEnabled(false);
    }

    public interface AddWishDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, WishItem item);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
