package com.mysaasa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.model.Message;
import com.mysaasa.api.model.PushMessageModel;
import com.mysaasa.api.responses.GetMessageByIdResponse;
import com.mysaasa.ui.ActivityChat;
import com.mysaasa.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by adam on 15-02-13.
 */
public class ReceiveGCMPush extends BroadcastReceiver {
    final static Gson gson = new Gson();

    @Override
    public void onReceive(Context context, Intent intent) {
        //We get a push, that means update messages for now
        MySaasaClient s = MySaasaApplication.getService();
        PushMessage type;
        Map<String, String> myMap;
        try {
            String sType = intent.getExtras().getString("class");
            String mapJson = intent.getExtras().getString("data");
            Type maptype = new TypeToken<Map<String, String>>() {
            }.getType();
            myMap = gson.fromJson(mapJson, maptype);
            type = PushMessage.valueOf(sType);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Envelope<PushMessage> envelope = new Envelope(type, myMap);
        MySaasaApplication.getService().bus.post(envelope);
        if (!envelope.isOpened()) {
            notifyUserOfMessage(context, envelope);
        }
    }

    private void notifyUserOfMessage(Context context, Envelope<PushMessage> envelope) {
        final Handler mHandler = new Handler(Looper.getMainLooper());
        final Map<String, String> data = envelope.getData();
        Call<GetMessageByIdResponse> call = MySaasaApplication
                .getService()
                .getGateway()
                .getMessageById(Long.valueOf(data.get("id")));


        new Thread(()->{
            Message m = null;

            try {
                m = call.execute().body().getData();
            } catch (IOException e) {
                return;
            }
            final Message finalM = m;

            mHandler.post(()->{
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle("New Message Received")
                        .setContentText(finalM.toString())
                        .setContentIntent(PendingIntent.getActivities(context, 0, new Intent[]{ActivityChat.getChatIntent(context,finalM)}, PendingIntent.FLAG_UPDATE_CURRENT))
                        .build();
                nm.notify(100, notification);
            });

        }).start();
    }

    //The types of messages we are expecting
    public enum PushMessage {MessageCreatedPushMessage,MessageThreadUpdated}



}
