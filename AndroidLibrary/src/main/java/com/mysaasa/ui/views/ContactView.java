package com.mysaasa.ui.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mysaasa.MySaasaApplication;
import com.mysassa.R;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Adam on 1/12/2015.
 */
public class ContactView extends FrameLayout {
    EditText name, email, phone, body;
    Button send;
    ContactViewCallbacks callbacks;
    private String toUser = "admin";

    public ContactView(Context context) {
        super(context);
        addView(onCreateView());
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(onCreateView());
    }

    public ContactViewCallbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(ContactViewCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public View onCreateView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.contact_view, this,false);
        name = (EditText) v.findViewById(R.id.name);
        email = (EditText) v.findViewById(R.id.email);
        phone = (EditText) v.findViewById(R.id.phone);
        body = (EditText) v.findViewById(R.id.body);
        send = (Button) v.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    name.setEnabled(false);
                    email.setEnabled(false);
                    phone.setEnabled(false);
                    body.setEnabled(false);
                    send.setEnabled(false);
                    MySaasaApplication
                            .getService()
                            .getMessagesManager()
                            .sendMessage(toUser,
                                    "App Feedback",
                                    body.getText().toString(),
                                    name.getText().toString(),
                                    email.getText().toString(),
                                    phone.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response->{
                                        if (callbacks != null) {
                                            callbacks.success();
                                        } else {
                                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    e->{
                                        if (callbacks != null) {
                                            callbacks.fail(e);
                                        } else {
                                            Toast.makeText(getContext(), "Error sending message " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                }
            }
        });
        return v;
    }



    private boolean validate() {
        boolean success = true;
        if (TextUtils.isEmpty(name.getText())) { success = false; name.setError("Name is required"); }
        if (TextUtils.isEmpty(email.getText()))  { success = false; email.setError("Email is required"); }
        if (TextUtils.isEmpty(body.getText()))  { success = false; body.setError("Body is required"); }

        return success;
    }


    public void setToUser(String toUser) {
        this.toUser = toUser;
    }


    public interface ContactViewCallbacks {
        void success();
        void fail(Throwable e);
    }
}
