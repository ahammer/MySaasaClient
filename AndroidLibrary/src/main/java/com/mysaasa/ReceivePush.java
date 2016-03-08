package com.mysaasa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mysaasa.ui.ActivityMain;
import com.mysaasa.ui.ActivityMessages;
import com.mysaasa.ui.SideNavigationCompatibleActivity;
import com.mysassa.R;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.messages.ThreadUpdatedPushMessage;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by adam on 15-02-13.
 */
public class ReceivePush extends BroadcastReceiver {
    enum Types {MessageCreatedPushMessage,MessageThreadUpdated}
    @Override
    public void onReceive(Context context, Intent intent) {
        //We get a push, that means update messages for now
        MySaasaClient s = MySaasaAndroidApplication.getService();
        Types type;
        try {
            String sType = intent.getExtras().getString("class");
            type = Types.valueOf(sType);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Toast.makeText(context, "Received a push: "+intent, Toast.LENGTH_LONG).show();

        switch (type) {

               case MessageCreatedPushMessage:
                   HandleNewMessage(context, s);
                   break;
               case MessageThreadUpdated:
                   s.bus.post(new ThreadUpdatedPushMessage());
                   break;
        }

    }

    private void HandleNewMessage(Context context, MySaasaClient s) {
        if (s!=null) {
            s.getMessagesManager().getMessageCount()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {

                }
            });
        }

        if (!SideNavigationCompatibleActivity.isInForeground()) {
            Notification.Builder mBuilder =
                    new Notification.Builder(context)
                            .setSmallIcon(R.drawable.ic_app)
                            .setContentTitle(context.getString(R.string.app_name)+": New Message")
                            .setContentText("Click to goto your inbox.");
            Intent resultIntent = new Intent(context, ActivityMessages.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ActivityMain.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(5005, mBuilder.build());

        } else {

        }
    }

}
