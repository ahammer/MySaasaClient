package com.mysaasa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mysaasa.api.MySaasaClient;

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

        Envelope envelope = new Envelope(type, json);
        MySaasaApplication.getService().bus.post(envelope);
        if (!envelope.isOpened()) {
            Toast.makeText(MySaasaApplication.getInstance(), "Message Received (No Foreground): "
                    + envelope.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //The types of messages we are expecting
    public enum PushMessage {MessageCreatedPushMessage,MessageThreadUpdated}

}
