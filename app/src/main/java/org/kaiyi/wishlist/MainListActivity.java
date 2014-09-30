package org.kaiyi.wishlist;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.kaiyi.wishlist.database.WishListDbHelper;
import org.kaiyi.wishlist.fragment.AddShopWishDialogFragment;
import org.kaiyi.wishlist.fragment.MainListFragment;
import org.kaiyi.wishlist.pojo.WishItem;


public class MainListActivity extends ActionBarActivity implements AddShopWishDialogFragment.AddWishDialogListener, ActionBar.OnNavigationListener{

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

        if(id == R.id.action_new) {
            addShopWishDialogFragment.show(getFragmentManager(), "addShopWishDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, WishItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(WishListDbHelper.WishListEntry.TABLE_NAME, null, item.getValues());
        Log.d(TAG, "insert new item id = " + id);
        mainListFragment.dataChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        if (position == 0) {
            mainListFragment.dataChanged(null);
            return true;
        } else if (position == 1) {
            mainListFragment.dataChanged(true);
            return true;
        } else if (position == 2) {
            mainListFragment.dataChanged(false);
            return true;
        }

        return false;
    }
}
