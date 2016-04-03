package com.mysassa.api;

import com.mysassa.api.model.Message;
import com.mysassa.api.observables.StandardMySaasaObservable;
import com.mysassa.api.responses.GetMessageCountResponse;
import com.mysassa.api.responses.SendMessageResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 4/6/2015.
 */
public class MessageManager {
    private final MySaasaClient mySaasa;

    public MessageManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }





    public Observable<GetMessageCountResponse> getMessageCount() {
        return Observable.create(new StandardMySaasaObservable<GetMessageCountResponse>(mySaasa) {
            @Override
            protected Call<GetMessageCountResponse> getNetworkCall() {
                return mySaasa.gateway.getMessageCount();
            }
        });
    }

    public void requestMessageThread(Message rootMessage) {

    }

    public Observable<SendMessageResponse> sendMessage(final String to_user,
                                                       final String title,
                                                       final String body,
                                                       final String name,
                                                       final String email,
                                                       final String phone) {
        return Observable.create(new StandardMySaasaObservable<SendMessageResponse>(mySaasa) {
            @Override
            protected Call<SendMessageResponse> getNetworkCall() {
                return mySaasa.gateway.sendMessage(to_user, title, body, name, email, phone);
            }
        }).subscribeOn(Schedulers.io());
    }
}
