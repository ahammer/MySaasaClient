package com.mysaasa.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mysaasa.MySaasaAndroidApplication;
import com.mysaasa.ui.views.ExtendedMessageView;
import com.mysassa.R;
import com.mysassa.api.model.Message;

import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 2/16/2015.
 */
public class ActivityMessage extends SideNavigationCompatibleActivity {
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
        MySaasaAndroidApplication.getService().getMessagesManager().requestMessageThread(rootMessage);
        threadList = (ListView) findViewById(R.id.list);
        replyButton = (Button) findViewById(R.id.post_reply_button);
        replyText = (EditText) findViewById(R.id.reply_text);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setupList();
    }

    private void setupList() {
        thread = Collections.emptyList();
        //TODO fill this list up with a thread or something
        // MySaasaAndroidApplication.getService().getState().messages.getThread(rootMessage);
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




    public static void start(Context context, Message m) {
        Intent i = new Intent(context, ActivityMessage.class);
        i.putExtra("message", (java.io.Serializable) m);
        context.startActivity(i);
    }
}
