package com.mysaasa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.PushNotifiedNewMessageEvent;
import com.mysassa.R;
import com.mysassa.api.messages.NewMessage;
import com.mysassa.api.model.Message;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Adam on 4/5/2016.
 */
public class ActivityChat extends Activity {
    public static final String EXTRA_MESSAGE = "Message";

    ListView list;
    EditText text;
    Button reply;

    private Message message;

    public static void StartChat(Context ctx, Message m) {
        Intent intent = new Intent(ctx, ActivityChat.class);
        intent.putExtra(EXTRA_MESSAGE, m);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        list = (ListView) findViewById(R.id.list);
        reply = (Button) findViewById(R.id.reply);
        text = (EditText) findViewById(R.id.text);
        message = (Message) getIntent().getSerializableExtra(EXTRA_MESSAGE);
        reply.setOnClickListener(click->submitPost());
        checkNotNull(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMessageThread();
        MySaasaApplication.getService().bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MySaasaApplication.getService().bus.unregister(this);
    }

    @Subscribe
    public void onNewMessageReceived(NewMessage event) {
        refreshMessageThread();
    }

    private void submitPost() {
        MySaasaApplication.getService().getMessagesManager()
                .replyToMessage(message, text.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result-> successfullySent(),
                        e-> failedToSend(e));
        setOptionsEnabled(false);
    }

    private void refreshMessageThread() {
        MySaasaApplication
                .getService()
                .getMessagesManager()
                .getMessageThread(message)
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(list -> setList(list));
    }


    private void setOptionsEnabled(boolean enabled) {
        reply.setEnabled(enabled);
        text.setEnabled(enabled);
    }

    private void successfullySent() {
        refreshMessageThread();
        setOptionsEnabled(true);
    }

    private void failedToSend(Throwable e) {
        Crouton.makeText(this, e.getMessage(), Style.INFO).show();
        setOptionsEnabled(true);
    }

    private void setList(List<Message> list) {
        this.list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return list.get(position).id;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(ActivityChat.this);
                tv.setText(list.get(position).body);
                return tv;

            }
        });
    }
}
