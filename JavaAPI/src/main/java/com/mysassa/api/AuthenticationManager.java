package com.mysassa.api;

import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.model.User;
import com.mysassa.api.observables.CreateAccountObservable;
import com.mysassa.api.observables.LoginSubscriber;
import com.mysassa.api.responses.CreateUserResponse;
import com.mysassa.api.responses.LoginUserResponse;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 3/3/2016.
 */
public class AuthenticationManager {
    public final MySaasaClient mySaasa;

    private LoginSubscriber loginObservable;
    private CreateAccountObservable createObservable;


    public AuthenticationManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }

    /**
     * Log the lastAuthenticatedUser in. Will not execute if already in progress,
     *
     * @param username
     * @param password
     */
    public Observable<LoginUserResponse> login(final String username, final String password) {
        return Observable.create(loginObservable = new LoginSubscriber(this, username, password));
    }

    public Observable<CreateUserResponse> createAccount(final String username, final String password) {
        return Observable.create(createObservable = new CreateAccountObservable(this, username, password)).subscribeOn(Schedulers.io());
    }

    public void signOut() {
        loginObservable = null;
        createObservable = null;
        mySaasa.bus.post(new LoginStateChanged());
    }

    public User getAuthenticatedUser() {
        if (createObservable != null) return createObservable.getResponse().getData();
        if (loginObservable != null) return loginObservable.getResponse().getData();
        return null;
    }

}
