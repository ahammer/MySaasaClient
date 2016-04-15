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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Adam on 1/12/2015.
 */
public class ContactView extends FrameLayout {
    //For testing, to force a particular Contact User
    public static String GLOBAL_CONTACT_USER_OVERRIDE = null;
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

        //Debug click listener
        v.findViewById(R.id.title).setOnClickListener(v1 -> {
                setFormEnabled(true);
                body.setText("Random Body "+System.currentTimeMillis());
                email.setText("Test"+System.currentTimeMillis()+"@blaaah12321.com");
                name.setText("Mr Tester"+System.currentTimeMillis());
                phone.setText("6041234322");
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    setFormEnabled(false);
                    if (MySaasaApplication.getService().getAuthenticationManager().getAuthenticatedUser() == null) {
                        SecureRandom random = new SecureRandom();
                        String password = new BigInteger(130, random).toString(32);

                        MySaasaApplication.getService().getAuthenticationManager()
                                .createAccount(email.getText().toString(), password)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(success->sendMessage());
                    } else {
                        sendMessage();
                    }
                }
            }
        });
        return v;
    }

    private void setFormEnabled(boolean enabled) {
        name.setEnabled(enabled);
        email.setEnabled(enabled);
        phone.setEnabled(enabled);
        body.setEnabled(enabled);
        send.setEnabled(enabled);
    }

    private void sendMessage() {
        String to = GLOBAL_CONTACT_USER_OVERRIDE!=null?GLOBAL_CONTACT_USER_OVERRIDE:toUser;
        MySaasaApplication
                .getService()
                .getMessagesManager()
                .sendMessage(to,
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
