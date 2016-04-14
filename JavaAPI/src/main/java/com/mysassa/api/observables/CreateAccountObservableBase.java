package com.mysassa.api.observables;

import com.mysassa.api.AuthenticationManager;
import com.mysassa.api.messages.LoginStateChanged;
import com.mysassa.api.responses.CreateUserResponse;
import com.mysassa.api.responses.RegisterGcmKeyResponse;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Adam on 3/26/2016.
 */
public class CreateAccountObservableBase extends StandardMySaasaObservable<CreateUserResponse> {
    private AuthenticationManager authenticationManager;
    private final String username;
    private final String password;

    public CreateAccountObservableBase(AuthenticationManager authenticationManager, String username, String password) {
        super(authenticationManager.mySaasa, false);
        this.authenticationManager = authenticationManager;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    protected Call<CreateUserResponse> getNetworkCall() {
        return this.getMySaasa().getGateway().createUser(this.username, this.password);
    }

    @Override
    public boolean postResponse(CreateUserResponse response) {
        getMySaasa().bus.post(new LoginStateChanged());
        PushIdGenerator generator = authenticationManager.pushIdGenerator;
        if (generator != null) {
            String pushId = generator.getPushId();
            System.out.println(pushId);
            if (pushId != null) {
                Call<RegisterGcmKeyResponse> call = authenticationManager.mySaasa.getGateway().registerGcmKey(pushId);
                try {
                    Response<RegisterGcmKeyResponse> registerResponse = call.execute();
                    return registerResponse.isSuccess();
                } catch (Exception e) {/*Do Nothing*/}
            }
        }
        return true;
    }
}
