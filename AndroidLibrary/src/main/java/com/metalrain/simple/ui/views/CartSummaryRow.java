package com.metalrain.simple.ui.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metalrain.simple.R;

/**
 * Created by adam on 2014-11-01.
 */
public class CartSummaryRow extends FrameLayout {


    TextView title, value;

    public CartSummaryRow(Context context, String title, String value) {
        super(context);
        inflate(context, R.layout.listitem_cart_summary_row,this);
        this.title = (TextView)findViewById(R.id.title);
        this.value = (TextView)findViewById(R.id.value);
        this.title.setText(title);
        this.value.setText(value);

    }

}
