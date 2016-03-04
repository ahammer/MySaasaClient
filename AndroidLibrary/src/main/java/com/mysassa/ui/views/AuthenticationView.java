package com.mysassa.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.LoginUserResponse;
import com.mysassa.api.MySaasaClient;
import com.mysassa.ui.ActivityMessages;
import com.mysassa.ui.ActivitySignin;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 1/4/2015.
 */
public class AuthenticationView extends FrameLayout {


    public AuthenticationView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_authentication,this);

        Button signin = (Button) findViewById(R.id.signin);
        Button signout = (Button) findViewById(R.id.logout);
        Button messages = (Button) findViewById(R.id.message);
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
            Observable<LoginUserResponse> observable = mySaasaClient.getLoginManager().getLastLoginResponseObservable();
            if (observable != null) {
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers
                        .mainThread())
                        .subscribe(new Action1<LoginUserResponse>() {
                            @Override
                            public void call(LoginUserResponse loginUserResponse) {
                                Toast.makeText(getContext(), "Am I logged in"+loginUserResponse.isSuccess(), Toast.LENGTH_SHORT).show();

                            }
                });
            }
        }
    }

    public AuthenticationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

}
