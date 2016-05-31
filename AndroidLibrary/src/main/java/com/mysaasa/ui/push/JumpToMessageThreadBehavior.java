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
import com.mysaasa.api.responses.GetMessageByIdResponse;
import com.mysaasa.ui.ActivityChat;
import com.mysassa.R;

import java.io.IOException;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import retrofit2.Call;

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
        new Thread(()->{
            ReceiveGCMPush.PushMessage msg = message.open();
            final Map<String, String> data = message.getData();
            Call<GetMessageByIdResponse> call = MySaasaApplication
                    .getService()
                    .getGateway()
                    .getMessageById(Long.valueOf(data.get("id")));

            Message m = null;
            try {
                m = call.execute().body().getData();
            } catch (IOException e) {
                return;
            }
            final Message finalM = m;

            activity.runOnUiThread(()->{
                ViewGroup v = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.crouton_new_message,null);
                TextView title = (TextView) v.findViewById(R.id.title);
                TextView body = (TextView) v.findViewById(R.id.body);

                title.setText(data.get("title"));
                body.setText(data.get("sender"));



                v.setOnClickListener(v1 -> {
                    ActivityChat.StartChat(activity, finalM.messageThreadRoot != null?finalM.messageThreadRoot:finalM);
                });

                Crouton.make(activity, v).show();

            });

        }).start();

    }

    public final void start() {
        MySaasaApplication.getService().bus.register(this);
    }

    public final void stop() {
        MySaasaApplication.getService().bus.unregister(this);
    }
}
