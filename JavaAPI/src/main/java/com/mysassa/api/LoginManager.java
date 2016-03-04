package com.mysassa.api;

import java.io.IOException;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/3/2016.
 */
public class LoginManager {
    private final MySaasaClient mySaasa;

    private Observable<LoginUserResponse> lastLoginResponseObservable;

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
        lastLoginResponseObservable = Observable.create(new LoginObservable(username, password));
        return lastLoginResponseObservable;
    }

    public Observable<LoginUserResponse> getLastLoginResponseObservable() {
        return lastLoginResponseObservable;
    }

    private class LoginObservable implements Observable.OnSubscribe<LoginUserResponse> {
        private LoginUserResponse lastResponse;
        private final String username;
        private final String password;

        public LoginObservable(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void call(Subscriber<? super LoginUserResponse> subscriber) {
            if (!subscriber.isUnsubscribed()) {
                if (lastResponse != null) {
                    subscriber.onNext(lastResponse);
                    subscriber.onCompleted();
                    return;
                }
                Call<LoginUserResponse> loginUserResponseCall = mySaasa.gateway.loginUser(username, password);
                try {
                    lastResponse = loginUserResponseCall.execute().body();
                    subscriber.onNext(lastResponse);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }

        }

    }

}
