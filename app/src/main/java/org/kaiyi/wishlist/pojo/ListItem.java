package org.kaiyi.wishlist.pojo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kaiyi on 9/7/14.
 */
public interface ListItem {
    View createView(Context context, ViewGroup root);

    void bindView(View contertView, int position);
}
