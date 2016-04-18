package com.mysaasa.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysassa.R;
import com.mysaasa.api.model.ContactInfo;

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
