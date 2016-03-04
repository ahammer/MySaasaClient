package com.mysassa.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.messages.SigninMessage;
import com.mysassa.api.messages.SignoutMessage;
import com.mysassa.ui.ActivityMessages;
import com.mysassa.ui.ActivitySignin;

/**
 * Created by Adam on 1/4/2015.
 */
public class AuthenticationView extends FrameLayout {
    private TextView title;
    private Button signin,  signout, messages;

    public AuthenticationView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_authentication,this);
        title = (TextView) findViewById(R.id.user);
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
                MySaasaAndroidApplication.getInstance().clearCredentials();

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
            MySaasaClient mySaasaClient = MySaasaAndroidApplication.getService();
            /* TODO handle this logic
            signin.setVisibility(mySaasaClient.getState().authenticated?View.GONE:View.VISIBLE);
            signout.setVisibility(!mySaasaClient.getState().authenticated?View.GONE:View.VISIBLE);
            title.setVisibility(!mySaasaClient.getState().authenticated ? View.GONE : View.VISIBLE);
            if (mySaasaClient.getState().user!=null) {
                title.setText(mySaasaClient.getState().user.identifier);
            }
            */
        }
    }

    public AuthenticationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void onLoginResult(SigninMessage m) {
        refreshInActivity();
    }

    public void onLogoutResult(SignoutMessage m) {
        refreshInActivity();
    }

    //Refresh in the Activities thread
    private void refreshInActivity() {
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
