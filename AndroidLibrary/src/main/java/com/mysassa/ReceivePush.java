package com.mysassa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mysassa.api.Service;
import com.mysassa.api.messages.NewMessage;
import com.mysassa.ui.ActivityBase;
import com.mysassa.ui.ActivityMain;
import com.mysassa.ui.ActivityMessages;

/**
 * Created by adam on 15-02-13.
 */
public class ReceivePush extends BroadcastReceiver {
    enum Types {MessageCreatedPushMessage,MessageThreadUpdated}
    @Override
    public void onReceive(Context context, Intent intent) {
        //We get a push, that means update messages for now
        Service s = SimpleApplication.getService();
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

    private void HandleNewMessage(Context context, Service s) {
        if (s!=null) {
            s.getMessageCount();
        }

        if (!ActivityBase.isInForeground()) {
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
            s.bus.post(new NewMessage());
        }
    }

}
