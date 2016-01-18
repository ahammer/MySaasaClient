package com.metalrain.simple.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.metalrain.simple.R;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.Service;
import com.metalrain.simple.api.messages.SigninMessage;
import com.metalrain.simple.api.messages.SignoutMessage;
import com.metalrain.simple.ui.ActivityMessages;
import com.metalrain.simple.ui.ActivitySignin;

import rx.functions.Action1;

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
                SimpleApplication.getInstance().clearCredentials();
                SimpleApplication.getService().signout();
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
        if (SimpleApplication.getInstance()!=null) {
            Service service = SimpleApplication.getService();
            signin.setVisibility(service.getState().authenticated?View.GONE:View.VISIBLE);
            signout.setVisibility(!service.getState().authenticated?View.GONE:View.VISIBLE);
            title.setVisibility(!service.getState().authenticated ? View.GONE : View.VISIBLE);
            if (service.getState().user!=null) {
                title.setText(service.getState().user.identifier);
            }
        }
    }

    public AuthenticationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (SimpleApplication.getInstance() != null) {
            SimpleApplication.getService().bus.toObserverable().subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    if (o instanceof  SigninMessage) {
                        onLoginResult((SigninMessage) o);
                    } else if (o instanceof SignoutMessage) {
                        onLogoutResult((SignoutMessage) o);
                    }

                }
            });
        }
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
