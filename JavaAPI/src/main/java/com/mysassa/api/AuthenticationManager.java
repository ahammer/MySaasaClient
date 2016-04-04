package com.mysassa.api;

import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.model.User;
import com.mysassa.api.observables.CreateAccountObservableBase;
import com.mysassa.api.observables.LoginObservableBase;
import com.mysassa.api.observables.PushIdGenerator;
import com.mysassa.api.responses.CreateUserResponse;
import com.mysassa.api.responses.LoginUserResponse;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * This file handles authentication for the server.
 * You can login/create accounts and see if there is a authenticated user
 *
 * Created by Adam on 3/3/2016.
 */
public class AuthenticationManager {
    public final MySaasaClient mySaasa;

    private LoginObservableBase loginObservableBase;
    private CreateAccountObservableBase createAccountObservableBase;
    public PushIdGenerator pushIdGenerator;

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
        return Observable.create(loginObservableBase = new LoginObservableBase(this, username, password)).subscribeOn(Schedulers.io());
    }

    public Observable<CreateUserResponse> createAccount(final String username, final String password) {
        return Observable.create(createAccountObservableBase = new CreateAccountObservableBase(this, username, password)).subscribeOn(Schedulers.io());
    }

    public void signOut() {
        loginObservableBase = null;
        createAccountObservableBase = null;
        mySaasa.bus.post(new LoginStateChanged());
    }

    public User getAuthenticatedUser() {
        try {

            if (createAccountObservableBase != null)
                return createAccountObservableBase.getResponse().getData();

            if (loginObservableBase != null)
                return loginObservableBase.getResponse().getData();

        } catch (Exception e) {
            //Do Nothing
        }
        return null;
    }


    public void setPushIdGenerator(PushIdGenerator pushIdGenerator) {
        this.pushIdGenerator = pushIdGenerator;
    }
}
