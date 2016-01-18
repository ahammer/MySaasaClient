package com.metalrain.simple.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metalrain.simple.R;
import com.metalrain.simple.api.model.ContactInfo;
import com.metalrain.simple.ui.ActivityAddress;

/**
 * Created by Adam on 4/1/2015.
 */
public abstract class AddressView extends FrameLayout {
    private TextView title;
    private TextView address;

    public int requestCode = 1001;
    public AddressView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.simple_address_view,this);
        title = (TextView)findViewById(R.id.title);
        address = (TextView)findViewById(R.id.address);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked();
            }
        });
    }

    protected abstract void clicked();

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestRequestCodeode) {
        this.requestCode = requestRequestCodeode;
    }

    public void setTitle(String s) {
        title.setText(s);
    }
    public void setAddress(ContactInfo info) {
        address.setText(info.toString());
    }
}
