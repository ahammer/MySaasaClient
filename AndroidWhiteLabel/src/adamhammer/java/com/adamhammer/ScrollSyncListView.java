package com.adamhammer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Adam on 2/27/2016.
 */
public class ScrollSyncListView extends ListView {

    public ScrollSyncListView(Context context) {
        super(context);
    }

    public ScrollSyncListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public float getScrollYPercentage() {
        int extent = computeHorizontalScrollExtent();

        return (float)computeVerticalScrollOffset()/(computeVerticalScrollRange()-computeVerticalScrollExtent());
    }


}
