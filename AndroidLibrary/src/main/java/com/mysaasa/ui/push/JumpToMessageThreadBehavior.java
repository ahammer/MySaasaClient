package com.mysaasa.ui.push;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.Envelope;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.ReceiveGCMPush;
import com.mysaasa.api.model.Message;
import com.mysaasa.api.model.PushMessageModel;
import com.mysaasa.ui.ActivityChat;
import com.mysassa.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Created by Adam on 5/22/2016.
 */
public class JumpToMessageThreadBehavior {
    final Activity activity;

    public JumpToMessageThreadBehavior(Activity activity) {
        this.activity = activity;
    }

    @Subscribe
    public void onNewMessageEvent(Envelope<ReceiveGCMPush.PushMessage, PushMessageModel> message) {
        ReceiveGCMPush.PushMessage msg = message.open();
        ViewGroup v = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.crouton_new_message,null);

        Message data = message.getObject().getMessage();
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView body = (TextView) v.findViewById(R.id.body);
        title.setText(data.title);
        body.setText(data.body);
        v.setOnClickListener(v1 -> {
            ActivityChat.StartChat(activity, data);
        });

        Crouton.make(activity, v).show();

    }

    public final void start() {
        MySaasaApplication.getService().bus.register(this);
    }

    public final void stop() {
        MySaasaApplication.getService().bus.unregister(this);
    }
}
