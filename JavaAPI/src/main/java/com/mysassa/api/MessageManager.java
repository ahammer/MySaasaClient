package com.mysassa.api;

import com.mysassa.api.model.Message;
import com.mysassa.api.observables.ModelMySaasaObservable;
import com.mysassa.api.observables.StandardMySaasaObservable;
import com.mysassa.api.responses.GetMessageCountResponse;
import com.mysassa.api.responses.GetMessagesResponse;
import com.mysassa.api.responses.GetThreadResponse;
import com.mysassa.api.responses.RegisterGcmKeyResponse;
import com.mysassa.api.responses.ReplyMessageResponse;
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
        return Observable.create(new StandardMySaasaObservable<GetMessageCountResponse>(mySaasa, true) {
            @Override
            protected Call<GetMessageCountResponse> getNetworkCall() {
                return mySaasa.gateway.getMessageCount();
            }
        });
    }

    public Observable<SendMessageResponse> sendMessage(final String to_user,
                                                       final String title,
                                                       final String body,
                                                       final String name,
                                                       final String email,
                                                       final String phone) {
        return Observable.create(new StandardMySaasaObservable<SendMessageResponse>(mySaasa, true) {
            @Override
            protected Call<SendMessageResponse> getNetworkCall() {
                return mySaasa.gateway.sendMessage(to_user, title, body, name, email, phone);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Message> getMessageThread(final Message m) {
        return Observable.create(new ModelMySaasaObservable<Message, GetThreadResponse>(mySaasa, true) {
            @Override
            public void processItems(GetThreadResponse response, Subscriber<? super Message> subscriber) {
                for (Message m:response.data) subscriber.onNext(m);
                subscriber.onCompleted();
            }

            @Override
            protected Call<GetThreadResponse> getNetworkCall() {
                return mySaasa.gateway.getThread(m.id);
            }
        }).subscribeOn(Schedulers.io()).onBackpressureBuffer();
    }

    public Observable<Message> getMessages() {
        return Observable.create(new ModelMySaasaObservable<Message, GetMessagesResponse>(mySaasa, true) {
            @Override
            protected Call<GetMessagesResponse> getNetworkCall() {
                return mySaasa.gateway.getMessages(0,100,"timeSent","ASC");
            }

            @Override
            public void processItems(GetMessagesResponse response, Subscriber<? super Message> subscriber) {
                for (Message m:response.data) subscriber.onNext(m);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).onBackpressureBuffer();
    }

    public Observable<ReplyMessageResponse> replyToMessage(final Message parent, final String s) {
        return Observable.create(new StandardMySaasaObservable<ReplyMessageResponse>(mySaasa, true ) {
            @Override
            protected Call<ReplyMessageResponse> getNetworkCall() {
                return mySaasa.gateway.replyMessage(parent.id,s);
            }
        }).subscribeOn(Schedulers.io() );
    }
}
