package com.mysaasa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mysaasa.ui.ActivityMain;
import com.mysaasa.ui.SideNavigationCompatibleActivity;
import com.mysassa.R;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.messages.ThreadUpdatedPushMessage;
import com.mysassa.api.model.Category;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by adam on 15-02-13.
 */
public class ReceivePush extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //We get a push, that means update messages for now
        MySaasaClient s = MySaasaApplication.getService();
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
            s.bus.post(new PushNotifiedNewMessage());
        }


        if (!SideNavigationCompatibleActivity.isInForeground() || 1 == 1) {
            Notification.Builder mBuilder =
                    new Notification.Builder(context)
                            .setSmallIcon(R.drawable.ic_app)
                            .setContentTitle(context.getString(R.string.app_name)+": New Message")
                            .setContentText("Click to goto your inbox.");

            //Switch to goto Application

            Intent resultIntent = new Intent(context, ActivityMain.class);
            resultIntent.putExtra("category", new Category("Messages"));

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
            mNotificationManager.notify(5005, mBuilder.build());

        }
    }

    enum Types {MessageCreatedPushMessage,MessageThreadUpdated}

}
