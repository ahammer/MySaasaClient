package com.mysassa.api.observables;

import com.mysassa.api.AuthenticationManager;
import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.responses.LoginUserResponse;

import java.io.IOException;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/26/2016.
 */
public class LoginObservableBase extends StandardMySaasaObservable<LoginUserResponse> {
    private AuthenticationManager authenticationManager;

    final String username;
    final String password;

    public LoginObservableBase(AuthenticationManager authenticationManager, String username, String password) {
        super(authenticationManager.mySaasa);
        this.authenticationManager = authenticationManager;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Call<LoginUserResponse> getNetworkCall() {
        return getMySaasa().getGateway().loginUser(username,password);
    }

    @Override
    protected void onSuccess(LoginUserResponse response) {
        super.onSuccess(response);
        getMySaasa().bus.post(new LoginStateChanged());
        PushIdGenerator generator = authenticationManager.pushIdGenerator;
        if (generator != null) {
            String pushId = generator.getPushId();
        }
    }
}
