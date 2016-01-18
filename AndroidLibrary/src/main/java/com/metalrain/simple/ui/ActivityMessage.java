package com.metalrain.simple.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.metalrain.simple.R;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.ThreadUpdatedPushMessage;
import com.metalrain.simple.api.messages.BlogPostsModified;
import com.metalrain.simple.api.messages.MessagesUpdated;
import com.metalrain.simple.api.messages.ThreadUpdated;
import com.metalrain.simple.api.model.BlogPost;
import com.metalrain.simple.api.model.Message;
import com.metalrain.simple.ui.views.ExtendedMessageView;

import java.util.List;

/**
 * Created by Adam on 2/16/2015.
 */
public class ActivityMessage extends ActivityBase{
    ListView threadList;
    private Message rootMessage;
    private List<Message> thread;
    private Button replyButton;
    private EditText replyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        rootMessage = (Message)getIntent().getSerializableExtra("message");
        SimpleApplication.getService().requestMessageThread(rootMessage);
        threadList = (ListView) findViewById(R.id.list);
        replyButton = (Button) findViewById(R.id.post_reply_button);
        replyText = (EditText) findViewById(R.id.reply_text);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleApplication.getService().replyToMessage(rootMessage, replyText.getText().toString());
            }
        });
        setupList();
    }

    private void setupList() {
        thread = SimpleApplication.getService().getState().messages.getThread(rootMessage);
        threadList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return thread.size();
            }

            @Override
            public Object getItem(int i) {
                return thread.get(i);
            }

            @Override
            public long getItemId(int i) {
                return thread.get(i).id;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (view ==null) {
                    view = new ExtendedMessageView(ActivityMessage.this);
                }
                ((ExtendedMessageView)view).setMessage((Message) getItem(i));
                return view;
            }
        });
    }

    @Override
    protected void handleMessage(Object o) {
        super.handleMessage(o);
        if (o instanceof ThreadUpdated) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupList();
                }
            });
        } else if (o instanceof ThreadUpdatedPushMessage) {
            //Request a refresh on the thread
            SimpleApplication.getService().requestMessageThread(rootMessage);
        }
    }



    public static void start(Context context, Message m) {
        Intent i = new Intent(context, ActivityMessage.class);
        i.putExtra("message", (java.io.Serializable) m);
        context.startActivity(i);
    }
}
