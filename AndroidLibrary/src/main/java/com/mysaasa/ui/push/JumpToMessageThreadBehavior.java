package com.mysaasa.ui.push;

import android.app.Activity;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.ReceiveGCMPush;

/**
 * Created by Adam on 5/22/2016.
 */
public class JumpToMessageThreadBehavior extends DefaultPushBehavior {
    public JumpToMessageThreadBehavior(Activity activity) {
        super(activity);
    }

    @Subscribe
    public void onNewMessageEvent(ReceiveGCMPush.PushEnvelope message) {
        message.consume();
        activity.runOnUiThread(() -> {
           Toast.makeText(activity, "Message Received via Behavior: "+message.toString(), Toast.LENGTH_SHORT).show();
        });
    }
}
