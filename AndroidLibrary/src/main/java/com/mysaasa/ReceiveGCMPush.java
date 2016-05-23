package com.mysaasa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mysaasa.api.MySaasaClient;
import com.mysaasa.api.messages.ThreadUpdatedPushMessage;
import com.mysaasa.ui.ActivityMain;

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

        PushEnvelope envelope = new PushEnvelope(type);
        MySaasaApplication.getService().bus.post(envelope);
        if (!envelope.isConsumed()) {
            Toast.makeText(MySaasaApplication.getInstance(), "Message Received (No Foreground): "+type, Toast.LENGTH_SHORT).show();
        }
    }

    //The types of messages we are expecting
    public enum PushMessage {MessageCreatedPushMessage,MessageThreadUpdated}

    //An envelope for the message so we can see if it's been opened
    public class PushEnvelope {
        final PushMessage type;
        boolean consumed;

        public PushMessage getType() {
            return type;
        }

        public boolean isConsumed() {
            return consumed;
        }

        public void consume() {
            this.consumed = true;
        }

        public PushEnvelope(PushMessage type) {
            this.type = type;
        }

    }
}
