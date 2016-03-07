package com.mysassa.api;

import com.mysassa.api.model.Message;
import com.mysassa.api.responses.GetMessageCountResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 4/6/2015.
 */
public class MessageManager {
    private final MySaasaClient mySaasa;

    public MessageManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }





    public Observable<Integer> getMessageCount() {
        return Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<GetMessageCountResponse> request = mySaasa.gateway.getMessageCount();
                    try {
                        Response<GetMessageCountResponse> response = request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void requestMessageThread(Message rootMessage) {

    }

    public long sendMessage(String admin, String s, String s1, String s2, String s3, String s4) {
        return 0;
    }
}
