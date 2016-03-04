package com.mysassa.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Message;
import com.mysassa.api.model.ReplyMessage;
import com.mysassa.ui.fragments.EmptyListAdapter;
import com.mysassa.ui.views.StandardMessageView;

import java.io.Serializable;

/**
 * Created by Adam on 2/16/2015.
 */
public class ActivityMessages  extends SideNavigationCompatibleActivity {
    private ListView list;
    ProgressDialog asyncDialog;

    public static class State implements Serializable {
        boolean waiting = false;
        public ReplyMessage waitingFor = null;
    }

    State state = new State();
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state",state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncDialog = new ProgressDialog(ActivityMessages.this);

        if (savedInstanceState != null) {
            state = (State) savedInstanceState.getSerializable("state");
        }


        setContentView(R.layout.activity_messages);
        list = (ListView)findViewById(R.id.list);
        updateMessageAdapter();
        if (state.waiting) {
            asyncDialog.show();
        }
    }

    private void updateMessageAdapter() {
        final MySaasaClient mySaasaClient = MySaasaAndroidApplication.getService();
        /*
        if (mySaasaClient != null && mySaasaClient.getState().authenticated) {
            final MySaasaClient mySaasaClient1 = mySaasaClient;
            list.setAdapter(new BaseAdapter() {
                private final MySaasaClient mySaasaClient = mySaasaClient1;

                @Override
                public int getCount() {
                    return this.mySaasaClient.getState().messages.getRootMessages().size();
                }

                @Override
                public Object getItem(int i) {
                    return this.mySaasaClient.getState().messages.getRootMessages().get(i);
                }

                @Override
                public long getItemId(int i) {
                    return this.mySaasaClient.getState().messages.getRootMessages().get(i).id;
                }

                @Override
                public View getView(final int i, View view, final ViewGroup viewGroup) {
                    final Message m = (Message) getItem(i);
                    StandardMessageView standardMessageView= new StandardMessageView(viewGroup.getContext());
                    standardMessageView.setMessage(m);
                    standardMessageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (m.getType() != null) {
                                switch (m.getType()) {
                                    case Reply:
                                        ReplyMessage msg = (ReplyMessage) m.getDataObj();
                                        BlogPost blogPost = null;// = MySaasaAndroidApplication.getService().mBlogPostManager.getBlogPostById(msg.blogpost_id);
                                        if (blogPost == null) {
                                            state.waiting = true;
                                            state.waitingFor = msg;
                                            asyncDialog.show();
                                        } else {
                                            ActivityBlogPost.startActivity(viewGroup.getContext(), blogPost, blogPost.categories.get(0), msg.comment_id);
                                        }


                                        //ActivityBlogPost.startActivity(view.getContext(), );
                                        break;
                                    case Unknown:
                                        ActivityMessage.start(viewGroup.getContext(), m);
                                }
                            } else {
                                ActivityMessage.start(viewGroup.getContext(), m);
                            }
                        }
                    });
                    return standardMessageView;
                }
            });

        } else {
            list.setAdapter(new MessagesEmptyListAdapter());
        }
        */
    }

    public static void showMessages(Context ctx) {
        Intent i = new Intent(ctx, ActivityMessages.class);
        ctx.startActivity(i);

    }

    private static class MessagesEmptyListAdapter extends EmptyListAdapter {
        @Override
        protected void Clicked() {
            //Nothing to do
        }

        @Override
        protected String getText() {
            return "No Messages";
        }
    }
}
