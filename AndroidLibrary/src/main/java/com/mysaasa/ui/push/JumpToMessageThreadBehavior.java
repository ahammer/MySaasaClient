package com.mysaasa.ui.push;

import android.app.Activity;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.Envelope;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.ReceiveGCMPush;

/**
 * Created by Adam on 5/22/2016.
 */
public class JumpToMessageThreadBehavior {
    final Activity activity;

    public JumpToMessageThreadBehavior(Activity activity) {
        this.activity = activity;
    }

    @Subscribe
    public void onNewMessageEvent(Envelope<ReceiveGCMPush.PushMessage> message) {
        ReceiveGCMPush.PushMessage msg = message.open();
        activity.runOnUiThread(() -> {
           Toast.makeText(activity, "Message Received via Behavior: "+msg.toString(), Toast.LENGTH_SHORT).show();
        });
    }

    public final void start() {
        MySaasaApplication.getService().bus.register(this);
    }

    public final void stop() {
        MySaasaApplication.getService().bus.unregister(this);
    }
}
