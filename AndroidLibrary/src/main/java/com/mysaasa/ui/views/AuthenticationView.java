package com.mysaasa.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.ActivityMain;
import com.mysaasa.ui.ActivitySignin;
import com.mysaasa.ui.fragments.MessagesFragment;
import com.mysassa.R;
import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.model.Category;
import com.mysassa.api.model.User;

/**
 * Created by Adam on 1/4/2015.
 */
public class AuthenticationView extends FrameLayout {
    private TextView username;
    private Button signout;
    private Button signin;

    public AuthenticationView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_authentication,this);

        username = (TextView) findViewById(R.id.user);
        signin = (Button) findViewById(R.id.signin);
        signout = (Button) findViewById(R.id.logout);

        signin.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), ActivitySignin.class);
            getContext().startActivity(i);
        });

        signout.setOnClickListener(view -> MySaasaApplication.getService().getAuthenticationManager().signOut());
        setVisibilities();
    }


    private void setVisibilities() {
        if (MySaasaApplication.getInstance()!=null) {
            User u = MySaasaApplication.getService().getAuthenticationManager().getAuthenticatedUser();
            boolean auth = u != null;
            username.setVisibility(u != null?VISIBLE:GONE);
            if (auth) {
                username.setText(u.identifier);
            }
            signin.setVisibility(auth?View.GONE:View.VISIBLE);
            signout.setVisibility(auth?View.VISIBLE:View.GONE);
        }
    }

    public AuthenticationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            MySaasaApplication.getService().bus.register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            MySaasaApplication.getService().bus.unregister(this);
        }
    }

    @Subscribe
    public void onLogin(LoginStateChanged message) {
        if (getContext() instanceof Activity) {
            Activity a = (Activity) getContext();
            a.runOnUiThread(() -> setVisibilities());
        }

    }
}
