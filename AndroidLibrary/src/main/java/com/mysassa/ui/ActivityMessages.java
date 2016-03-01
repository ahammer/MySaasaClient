package com.mysassa.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mysassa.R;
import com.mysassa.SimpleApplication;
import com.mysassa.api.Service;
import com.mysassa.api.messages.BlogPostsModified;
import com.mysassa.api.messages.MessagesUpdated;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Message;
import com.mysassa.api.model.ReplyMessage;
import com.mysassa.ui.fragments.EmptyListAdapter;
import com.mysassa.ui.views.StandardMessageView;

import java.io.Serializable;

/**
 * Created by Adam on 2/16/2015.
 */
public class ActivityMessages  extends ActivityBase{
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


    @Override
    protected void handleMessage(Object o) {
        super.handleMessage(o);
        if (o instanceof MessagesUpdated) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateMessageAdapter();
                }
            });
        } else if (o instanceof BlogPostsModified) {
            final BlogPostsModified msg = (BlogPostsModified) o;
            if (msg.blogResponse != null && msg.blogResponse.isSuccess()) {
                for (BlogPost post:msg.blogResponse.getData()) {
                    if (post.id == state.waitingFor.blogpost_id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                asyncDialog.hide();
                                BlogPost blogPost = SimpleApplication.getService().mBlogPostManager.getBlogPostById(state.waitingFor.blogpost_id);
                                if (blogPost != null) {
                                    ActivityBlogPost.startActivity(ActivityMessages.this, blogPost, blogPost.categories.get(0), state.waitingFor.comment_id);
                                    state.waitingFor=null;
                                }
                                state.waiting=false;
                            }
                        });
                    }
                }
            }
        }
    }

    private void updateMessageAdapter() {
        final Service service = SimpleApplication.getService();
        if (service != null && service.getState().authenticated) {
            final Service service1 = service;
            list.setAdapter(new BaseAdapter() {
                private final Service service = service1;

                @Override
                public int getCount() {
                    return service.getState().messages.getRootMessages().size();
                }

                @Override
                public Object getItem(int i) {
                    return service.getState().messages.getRootMessages().get(i);
                }

                @Override
                public long getItemId(int i) {
                    return service.getState().messages.getRootMessages().get(i).id;
                }

                @Override
                public View getView(final int i, View view, final ViewGroup viewGroup) {
                    final Message m = (Message) getItem(i);
                    StandardMessageView tv= new StandardMessageView(viewGroup.getContext());
                    tv.setMessage(m);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (m.getType() != null) {
                                switch (m.getType()) {
                                    case Reply:
                                        ReplyMessage msg = (ReplyMessage) m.getDataObj();
                                        BlogPost blogPost = SimpleApplication.getService().mBlogPostManager.getBlogPostById(msg.blogpost_id);
                                        if (blogPost == null) {
                                            SimpleApplication.getService().getBlogPostById(msg.blogpost_id);
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
                    return tv;
                }
            });
        } else {
            list.setAdapter(new MessagesEmptyListAdapter());
        }
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
