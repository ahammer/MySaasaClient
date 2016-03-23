package com.mysassa.api;

import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.model.User;
import com.mysassa.api.responses.LoginUserResponse;

import java.io.IOException;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 3/3/2016.
 */
public class LoginManager {
    private final MySaasaClient mySaasa;

    private LoginObservable loginSubscription;
    private Observable<LoginUserResponse> lastLoginResponseObservable;


    public LoginManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }

    /**
     * Log the lastAuthenticatedUser in. Will not execute if already in progress,
     *
     * @param username
     * @param password
     */
    public Observable<LoginUserResponse> login(final String username, final String password) {
        lastLoginResponseObservable = Observable.create(loginSubscription = new LoginObservable(username, password));
        lastLoginResponseObservable = lastLoginResponseObservable.subscribeOn(Schedulers.io());
        lastLoginResponseObservable.doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }).subscribe();
        return lastLoginResponseObservable;
    }

    public void signOut() {
        loginSubscription = null;
        lastLoginResponseObservable = null;
        mySaasa.bus.post(new LoginStateChanged());
    }

    public User getAuthenticatedUser() {
        try {
            return loginSubscription.lastResponse.getData();
        } catch (NullPointerException e) {
            return null;
        }
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
                    mySaasa.startNetwork();
                    lastResponse = loginUserResponseCall.execute().body();
                    if (lastResponse.isSuccess()) {
                        mySaasa.bus.post(new LoginStateChanged());
                    }

                    subscriber.onNext(lastResponse);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    mySaasa.stopNetwork();
                }
            }

        }
    }

}
