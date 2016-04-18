package com.mysaasa;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.messages.NewMessageInMemoryEvent;
import com.mysaasa.api.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Observes the new message notifications and handles when to give notifications to the user
 *
 * Created by Adam on 4/14/2016.
 */
public class MessageNotificationManager  {
    final MySaasaClient client;
    private final Application ctx;
    private final Handler handler;
    private emitEventToObservers emitter;
    private Subscription subscription;

    public MessageNotificationManager(Application ctx, MySaasaClient client) {
        checkNotNull(client);
        checkNotNull(ctx);
        this.client = client;
        this.ctx = ctx;
        handler = new Handler(ctx.getMainLooper());
    }

    public void start() {
        client.bus.register(this);
        final User user = client.getAuthenticationManager().getAuthenticatedUser();

        subscription = Observable.create(emitter = new emitEventToObservers())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(event -> !event.getMessage().sender.equals(user))
                .subscribe(this::handleMessage);
    }

    int index = 0;
    public void handleMessage(NewMessageInMemoryEvent event) {
        handler.post(()->{
            Toast.makeText(ctx, "New Message Received: "+index,Toast.LENGTH_SHORT).show();
            index++;
        });
    }

    public void stop() {
        client.bus.register(this);
        subscription.unsubscribe();
    }


    @Subscribe
    public void NewMessageEvent(NewMessageInMemoryEvent event) {
        emitter.emit(event);
    }


    private static class emitEventToObservers implements Observable.OnSubscribe<NewMessageInMemoryEvent> {
        List<Subscriber<? super NewMessageInMemoryEvent>> subscribers = new ArrayList<>();

        @Override
        public void call(Subscriber<? super NewMessageInMemoryEvent> subscriber) {
            subscribers.add(subscriber);
        }


        public void emit(NewMessageInMemoryEvent event) {
            for (Subscriber s:subscribers) {
                if (!s.isUnsubscribed()) {
                    s.onNext(event);
                }
            }
        }
    }
}
