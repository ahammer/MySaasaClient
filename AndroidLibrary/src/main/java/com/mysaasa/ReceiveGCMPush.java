package com.mysaasa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.messages.ThreadUpdatedPushMessage;

/**
 * Created by adam on 15-02-13.
 */
public class ReceiveGCMPush extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //We get a push, that means update messages for now
        MySaasaClient s = MySaasaApplication.getService();
        PushMessage type;
        try {
            String sType = intent.getExtras().getString("class");
            type = PushMessage.valueOf(sType);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        MySaasaApplication.getService().bus.post(type);

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
            MySaasaApplication.getService().getMessagesManager().getMessages().subscribe();
        }
    }

    public static enum PushMessage {MessageCreatedPushMessage,MessageThreadUpdated}

}
/**

 if (!SideNavigationCompatibleActivity.isInForeground()) {
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
 **/