package com.mysassa.api.observables;

import com.mysassa.api.AuthenticationManager;
import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.responses.CreateUserResponse;
import com.mysassa.api.responses.LoginUserResponse;

import java.io.IOException;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/26/2016.
 */
public class CreateAccountObservable extends StandardMySaasaObservable<CreateUserResponse> {
    private AuthenticationManager authenticationManager;
    private final String username;
    private final String password;

    public CreateAccountObservable(AuthenticationManager authenticationManager, String username, String password) {
        super(authenticationManager.mySaasa);
        this.authenticationManager = authenticationManager;
        this.username = username;
        this.password = password;
    }

    protected Call<CreateUserResponse> getNetworkCall() {
        return this.getMySaasa().getGateway().createUser(this.username, this.password);
    }

    @Override
    protected void onSuccess(CreateUserResponse response) {
        super.onSuccess(response);
        this.getMySaasa().bus.post(new LoginStateChanged());
    }
}
