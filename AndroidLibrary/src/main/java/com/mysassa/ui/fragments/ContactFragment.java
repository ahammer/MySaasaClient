package com.mysassa.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.model.User;

import rx.functions.Action1;

/**
 * Created by Adam on 1/12/2015.
 */
public class ContactFragment extends Fragment {
    EditText name, email, phone, body;
    Button send;

    private long requestCode = 0;
    private Action1<Object> messageHook;

    public ContactFragment() {
    }

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.fragment_contactinfo, container,false);
        name = (EditText) v.findViewById(R.id.name);
        email = (EditText) v.findViewById(R.id.email);
        phone = (EditText) v.findViewById(R.id.phone);
        body = (EditText) v.findViewById(R.id.message);
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

                    requestCode = MySaasaAndroidApplication.getService().getMessagesManager().sendMessage("admin", "App Feedback", body.getText().toString(), name.getText().toString(), email.getText().toString(), phone.getText().toString());
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

    @Override
    public void onResume() {
        super.onResume();

    }

}
