package com.mysaasa.api;

import com.mysaasa.api.model.User;
import com.mysaasa.api.observables.ModelMySaasaObservable;
import com.mysaasa.api.responses.GetMessageCountResponse;
import com.mysaasa.api.responses.GetMessagesResponse;
import com.mysaasa.api.responses.ReplyMessageResponse;
import com.mysaasa.api.responses.SendMessageResponse;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 4/6/2015.
 */
public class MessageManager {
    private final MySaasaClient mySaasa;
    private final MySaasaMessageStorage messageStore;

    public MessageManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
        messageStore = new InMemoryMessageStorage(mySaasaClient);
    }


    public Observable<GetMessageCountResponse> getMessageCount() {
        return Observable.create(new com.mysaasa.api.observables.StandardMySaasaObservable<GetMessageCountResponse>(mySaasa, true) {
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
        return Observable.create(new com.mysaasa.api.observables.StandardMySaasaObservable<SendMessageResponse>(mySaasa, true) {
            @Override
            protected Call<SendMessageResponse> getNetworkCall() {
                return mySaasa.gateway.sendMessage(to_user, title, body, name, email, phone);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<com.mysaasa.api.model.Message> getMessageThread(final com.mysaasa.api.model.Message m) {
        return Observable.create(new ModelMySaasaObservable<com.mysaasa.api.model.Message, com.mysaasa.api.responses.GetThreadResponse>(mySaasa, true) {
            @Override
            public void processItems(com.mysaasa.api.responses.GetThreadResponse response, Subscriber<? super com.mysaasa.api.model.Message> subscriber) {
                for (com.mysaasa.api.model.Message m:response.data) messageStore.storeMessage(m);
                for (com.mysaasa.api.model.Message message : response.data)
                    subscriber.onNext(message);

                subscriber.onCompleted();
            }

            @Override
            protected Call<com.mysaasa.api.responses.GetThreadResponse> getNetworkCall() {
                return mySaasa.gateway.getThread(m.id);
            }
        }).subscribeOn(Schedulers.io()).onBackpressureBuffer();
    }


    public Observable<com.mysaasa.api.model.Message> getMessages() {
        return Observable.create(new ModelMySaasaObservable<com.mysaasa.api.model.Message, GetMessagesResponse>(mySaasa, true) {
            @Override
            protected Call<GetMessagesResponse> getNetworkCall() {
                return mySaasa.gateway.getMessages(0,100,"timeSent","DESC");
            }

            @Override
            public void processItems(GetMessagesResponse response, Subscriber<? super com.mysaasa.api.model.Message> subscriber) {

                User user = mySaasa
                        .getAuthenticationManager()
                        .getAuthenticatedUser();

                messageStore.storeMessages(response.data);


                for (com.mysaasa.api.model.Message m : messageStore.getRootMessages(user))
                    subscriber.onNext(m);

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).onBackpressureBuffer();
    }

    public Observable<com.mysaasa.api.responses.ReplyMessageResponse> replyToMessage(final com.mysaasa.api.model.Message parent, final String s) {
        return Observable.create(new com.mysaasa.api.observables.StandardMySaasaObservable<ReplyMessageResponse>(mySaasa, true ) {
            @Override
            protected Call<com.mysaasa.api.responses.ReplyMessageResponse> getNetworkCall() {
                return mySaasa.gateway.replyMessage(parent.id,s);
            }
        }).subscribeOn(Schedulers.io() );
    }

}
