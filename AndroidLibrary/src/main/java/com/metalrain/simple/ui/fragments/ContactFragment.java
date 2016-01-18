package com.metalrain.simple.ui.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.metalrain.simple.R;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.MessageSuccessfullySent;
import com.metalrain.simple.api.messages.ProductCategoriesUpdatedMessage;
import com.metalrain.simple.api.model.User;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.functions.Action1;

import static de.keyboardsurfer.android.widget.crouton.Style.INFO;

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

                    requestCode = SimpleApplication.getService().sendMessage("admin", "App Feedback", body.getText().toString(), name.getText().toString(), email.getText().toString(), phone.getText().toString());
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
        SimpleApplication.getService().bus.toObserverable().subscribe(messageHook = new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof MessageSuccessfullySent) {
                    final MessageSuccessfullySent msg = (MessageSuccessfullySent)o;
                    if (msg.requestCode == requestCode) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.isSuccess()) {
                                    Crouton.makeText(getActivity(), "Successfully sent", Style.INFO).show();
                                } else {
                                    name.setEnabled(false);
                                    email.setEnabled(false);
                                    phone.setEnabled(false);
                                    body.setEnabled(false);
                                    send.setEnabled(false);
                                    Crouton.makeText(getActivity(), "Error Sending", Style.ALERT).show();
                                }

                            }
                        });
                    }
                }
            }
        });
    }

}
