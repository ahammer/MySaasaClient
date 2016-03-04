package com.mysassa.api;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/3/2016.
 */
public class LoginManager {
    private final MySaasaClient mySaasa;
    private Observable<LoginUserResponse> mLoginResponseObservable;

    public LoginManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }

    /**
     * Log the user in. Will not execute if already in progress,
     *
     * @param username
     * @param password
     */
    public Observable<LoginUserResponse> login(final String username, final String password) {
        if (mLoginResponseObservable != null) return mLoginResponseObservable;
        mLoginResponseObservable = Observable.create(new Observable.OnSubscribe<LoginUserResponse>() {
            @Override
            public void call(Subscriber<? super LoginUserResponse> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<LoginUserResponse> loginUserResponseCall = mySaasa.gateway.loginUser(username, password);
                    try {
                        Response<LoginUserResponse> response = loginUserResponseCall.execute();
                        subscriber.onNext(response.body());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }

            }
        }).cache();
        return mLoginResponseObservable;
    }

    public Observable<LoginUserResponse> getmLoginResponseObservable() {
        return mLoginResponseObservable;
    }
}
