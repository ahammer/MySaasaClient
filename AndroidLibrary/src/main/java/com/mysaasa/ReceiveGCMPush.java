package com.mysaasa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.model.Message;
import com.mysaasa.api.model.PushMessageModel;
import com.mysaasa.ui.ActivityChat;
import com.mysassa.R;

/**
 * Created by adam on 15-02-13.
 */
public class ReceiveGCMPush extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //We get a push, that means update messages for now
        MySaasaClient s = MySaasaApplication.getService();
        PushMessage type;
        String json = intent.getExtras().getString("json");
        try {
            String sType = intent.getExtras().getString("class");


            type = PushMessage.valueOf(sType);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Envelope<PushMessage, PushMessageModel> envelope = new Envelope(type, json, PushMessageModel.class);
        MySaasaApplication.getService().bus.post(envelope);
        if (!envelope.isOpened()) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_app)
                    .setContentTitle("New Message Received")
                    .setContentText(envelope.getObject().getMessage().toString())
                    .setContentIntent(PendingIntent.getActivities(context, 0, new Intent[]{ActivityChat.getChatIntent(context, envelope.getObject().getMessage())}, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
            nm.notify(100, notification);
        }
    }

    //The types of messages we are expecting
    public enum PushMessage {MessageCreatedPushMessage,MessageThreadUpdated}



}
