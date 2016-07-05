package com.mysaasa.ui.push;

import com.mysaasa.Envelope;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.ReceiveGCMPush;
import com.mysaasa.ui.ActivityChat;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Adam on 5/22/2016.
 */
public class RefreshThreadPushMessageBehavior {
    final ActivityChat activity;

    public RefreshThreadPushMessageBehavior(ActivityChat activity) {
        this.activity = activity;
    }

    @Subscribe
    public void onNewMessageEvent(Envelope<ReceiveGCMPush.PushMessage> message) {
        activity.runOnUiThread(()->{
            ReceiveGCMPush.PushMessage msg = message.open();
            activity.refreshMessageThread();
        });
    }

    public final void start() {
        MySaasaApplication.getService().bus.register(this);
    }

    public final void stop() {
        MySaasaApplication.getService().bus.unregister(this);
    }
}
