package com.mysaasa.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysaasa.R;

/**
 * Created by Adam on 1/13/2015.
 */
public class EmptyListItem extends FrameLayout {
    private TextView text;

    public EmptyListItem(Context context) {
        super(context);
        init();
    }

    public EmptyListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.listitem_emptyrow, this);
        text = (TextView) findViewById(R.id.text);
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
