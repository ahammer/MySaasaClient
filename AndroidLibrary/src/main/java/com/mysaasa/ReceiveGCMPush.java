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
        try {
            String sType = intent.getExtras().getString("class");
            type = PushMessage.valueOf(sType);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Envelope envelope = new Envelope(type);
        MySaasaApplication.getService().bus.post(envelope);
        if (!envelope.isOpened()) {
            Toast.makeText(MySaasaApplication.getInstance(), "Message Received (No Foreground): "+type, Toast.LENGTH_SHORT).show();
        }
    }

    //The types of messages we are expecting
    public enum PushMessage {MessageCreatedPushMessage,MessageThreadUpdated}

    //An envelope for the message so we can see if it's been opened
    public class Envelope<T> {
        final T contents;
        boolean opened;

        public Envelope(T contents) {
            this.contents = contents;
        }

        public T open() {
            opened = true;
            return contents;
        }

        public boolean isOpened() {
            return opened;
        }

    }
}
