package com.mysaasa;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.messages.NewMessageEvent;
import com.mysaasa.api.model.Message;
import com.mysaasa.api.model.User;
import com.mysaasa.ui.ActivityChat;
import com.mysaasa.ui.SideNavigationCompatibleActivity;
import com.mysassa.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Notification.*;
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

    private Subscription subscription;

    public MessageNotificationManager(Application ctx, MySaasaClient client) {
        checkNotNull(client);
        checkNotNull(ctx);
        this.client = client;
        this.ctx = ctx;
        handler = new Handler(ctx.getMainLooper());
    }

    public void start() {
        final User user = client.getAuthenticationManager().getAuthenticatedUser();

        subscription = client.getMessagesManager().getMessagesObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .debounce(700, TimeUnit.MILLISECONDS)
                .filter(event ->
                        event.getMessage().sender.equals(user)
                ).subscribe(msg->handleMessage((NewMessageEvent)msg));
    }
    int index = 0;
    public void handleMessage(NewMessageEvent event) {
        handler.post(()->{

            Toast.makeText(ctx, "New Message Received "+(index++), Toast.LENGTH_SHORT).show();
            showNotification(ctx, event.getMessage());
        });
    }

    private void showNotification(Application application, Message m) {

        Context context = MySaasaApplication.getInstance();
        if (context == null) {
            Builder mBuilder =
                    new Builder(application)
                            .setSmallIcon(R.drawable.ic_app)
                            .setContentTitle(application.getString(R.string.app_name) + ": New Message")
                            .setContentText("Click to goto chat.");



            mBuilder.setContentIntent(
                    PendingIntent.getActivity(
                            application,
                            1000,ActivityChat.getChatIntent(application, m), 0));
            NotificationManager mNotificationManager =
                    (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(5005, mBuilder.build());
        } else {
            ActivityChat.StartChat(context,m);
        }



    }

    public void stop() {
        subscription.unsubscribe();
    }




}
