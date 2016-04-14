package com.mysassa.api;

import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.model.User;
import com.mysassa.api.observables.CreateAccountObservableBase;
import com.mysassa.api.observables.LoginObservableBase;
import com.mysassa.api.observables.PushIdGenerator;
import com.mysassa.api.responses.CreateUserResponse;
import com.mysassa.api.responses.LoginUserResponse;

import java.util.Date;

import rx.Observable;
import rx.Subscription;
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

    public LoginUserResponse.SessionSummary getSessionSummary() {
        try {
            if (createAccountObservableBase != null)
                return createAccountObservableBase.getResponse().getData();

            if (loginObservableBase != null)
                return loginObservableBase.getResponse().getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getAuthenticatedUser() {
        try {
            return getSessionSummary().getContext().getUser();
        } catch (Exception e) {
            e.printStackTrace();
            //Do Nothing
        }
        return null;
    }


    public void setPushIdGenerator(PushIdGenerator pushIdGenerator) {
        this.pushIdGenerator = pushIdGenerator;
    }

    //Blocking Call, call from another thread
    //This signs in you in again, if the session is expired.
    public void refreshIfNecessary() {
        LoginUserResponse.SessionSummary sessionSummary = getSessionSummary();
        if (sessionSummary != null) {
            int seconds = sessionSummary.getLengthSeconds();
            final Date expiry = new Date(sessionSummary.getTimestamp().getTime() + (seconds * 1000));
            if (expiry.before(new Date())) {
                if (createAccountObservableBase != null) {
                    login(createAccountObservableBase.getUsername(), createAccountObservableBase.getPassword())
                            .subscribeOn(Schedulers.immediate())
                            .observeOn(Schedulers.immediate())
                            .subscribe(result->{
                                System.out.println("Success");
                            });
                } else if (loginObservableBase != null) {
                    login(loginObservableBase.getUsername(), loginObservableBase.getPassword())
                            .subscribeOn(Schedulers.immediate())
                            .observeOn(Schedulers.immediate())
                            .subscribe(result->{
                                System.out.println("Success");
                            });
                }

            }
        }
    }



}
