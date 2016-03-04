package com.mysassa.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.common.eventbus.Subscribe;
import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.model.User;
import com.mysassa.ui.ActivityMessages;
import com.mysassa.ui.ActivitySignin;

/**
 * Created by Adam on 1/4/2015.
 */
public class AuthenticationView extends FrameLayout {


    private Button signout;
    private Button signin;
    private Button messages;

    public AuthenticationView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_authentication,this);

        signin = (Button) findViewById(R.id.signin);
        signout = (Button) findViewById(R.id.logout);
        messages = (Button) findViewById(R.id.message);

        signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ActivitySignin.class);
                getContext().startActivity(i);
            }
        });

        signout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MySaasaAndroidApplication.getService().getLoginManager().signOut();

            }
        });

        messages.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityMessages.showMessages(AuthenticationView.this.getContext());
            }
        });


        setVisibilities();
    }


    private void setVisibilities() {
        if (MySaasaAndroidApplication.getInstance()!=null) {
            User u = MySaasaAndroidApplication.getService().getLoginManager().getAuthenticatedUser();
            boolean auth = u != null;
            signin.setVisibility(auth?View.GONE:View.VISIBLE);
            messages.setVisibility(auth?View.VISIBLE:View.GONE);
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
        MySaasaAndroidApplication.getService().bus.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MySaasaAndroidApplication.getService().bus.unregister(this);
    }

    @Subscribe
    public void onLogin(LoginStateChanged message) {
        if (getContext() instanceof Activity) {
            Activity a = (Activity) getContext();
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setVisibilities();
                }
            });
        }

    }
}
