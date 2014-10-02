package org.kaiyi.wishlist;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.fragment.AddShopWishDialogFragment;
import org.kaiyi.wishlist.fragment.MainListFragment;
import org.kaiyi.wishlist.pojo.WishItem;
import org.kaiyi.wishlist.utils.Constant;


public class MainListActivity extends ActionBarActivity implements AddShopWishDialogFragment.AddWishDialogListener, ActionBar.OnNavigationListener {

    private static final String TAG = MainListActivity.class.getName();

    AddShopWishDialogFragment addShopWishDialogFragment;
    MainListFragment mainListFragment;
    WishListDbHelper dbHelper;
    SpinnerAdapter mSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        addShopWishDialogFragment = new AddShopWishDialogFragment();
        addShopWishDialogFragment.setListener(this);

        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_spinner_list,
                android.R.layout.simple_spinner_dropdown_item);
        getSupportActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dbHelper = WishListDbHelper.getInstance(this);

    }

    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        mainListFragment = (MainListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new) {
            addShopWishDialogFragment.show(getSupportFragmentManager(), "addShopWishDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, WishItem item) {
        Uri uri = getContentResolver().insert(Uri.parse(Constant.URI.ITEMS), item.getValues());
        Log.d(TAG, "insert new item id = " + uri.getLastPathSegment());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        if (position == 0) {
            mainListFragment.loadList(Constant.LOADER_ID.LOAD_ITEMS_ALL);
            return true;
        } else if (position == 1) {
            mainListFragment.loadList(Constant.LOADER_ID.LOAD_ITEMS_COMPLETED);
            return true;
        } else if (position == 2) {
            mainListFragment.loadList(Constant.LOADER_ID.LOAD_ITEMS_NOT_COMPLETED);
            return true;
        }

        return false;
    }

}
