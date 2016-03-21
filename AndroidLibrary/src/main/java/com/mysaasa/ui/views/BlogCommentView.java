package com.mysaasa.ui.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysaasa.MySaasaApplication;
import com.mysassa.R;
import com.mysassa.api.CommentManager;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.User;
import com.mysaasa.ui.ActivityPostComment;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Adam on 1/6/2015.
 */
public abstract class BlogCommentView extends CardView {
    public static final String TREE_CLOSE = "-";
    public static final String TREE_OPEN = "+";
    public static final String TREE_NO_CHILDREN = "";
    BlogComment comment;
    private LinearLayout info;
    private TextView body;
    private TextView author;
    private TextView showChildren;

    private ImageView remove, vote, reply;

    private BlogDepthView depth;
    private ImageView edit;


    public BlogCommentView(Context context) {
        super(context);
        init();
    }

    public BlogCommentView(Context context, AttributeSet attrs) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(),R.layout.listitem_blogcomment,this);
        author = (TextView)findViewById(R.id.author);
        body = (TextView)findViewById(R.id.content);
        depth = (BlogDepthView) findViewById(R.id.depth);
        remove = (ImageView) findViewById(R.id.remove);
        vote = (ImageView) findViewById(R.id.vote);
        reply = (ImageView) findViewById(R.id.reply);
        edit = (ImageView) findViewById(R.id.edit);
        showChildren = (TextView)findViewById(R.id.showChildren);

        User u = null;
        if (u == null) {
            vote.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        }

        reply.setOnClickListener(view -> ActivityPostComment.postComment((Activity) getContext(), null, comment));
        edit.setOnClickListener(view -> ActivityPostComment.editComment((Activity) getContext(),comment));


        remove.setOnClickListener(view->{
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Deleting Comment")
                    .setMessage("Are you sure you want to delete?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        checkNotNull(comment);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    protected abstract BlogPost getBlogPost();

    public void setComment(final BlogComment comment) {
        this.comment = comment;
        if (comment.getAuthor() !=null) {
            author.setText("Posted by " + comment.getAuthor().identifier + " on " + comment.getDateCreated());
        }else  {
            author.setText("");
        }

        body.setText(comment.getContent());
        depth.setDepth(comment.calculateDepth(MySaasaApplication.getService().getCommentManager()));
        //TODO put Signed in user here
        User u = null;

        if (comment.getAuthor() == null) {                       //Only Deleted comments have null author
            remove.setVisibility(View.GONE);
            vote.setVisibility(View.GONE);
            reply.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        } else if (u!=null && u.id == comment.getAuthor().id){   //You are the author
            remove.setVisibility(View.VISIBLE);
            vote.setVisibility(View.GONE);
            reply.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        } else if (u == null) {                             //You are not signed in
            remove.setVisibility(View.GONE);
            vote.setVisibility(View.GONE);
            reply.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        } else {                                            //You are signed in, but it's another
            vote.setVisibility(View.GONE);               //persons comment
            reply.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }

        List<BlogComment> children = comment.getChildren(MySaasaApplication.getService().getCommentManager());
        if (children.size() == 0) {
            showChildren.setText(TREE_NO_CHILDREN);
            showChildren.setEnabled(false);
        } else {
            showChildren.setVisibility(View.VISIBLE);
            showChildren.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (showChildren.getText().equals(TREE_CLOSE)) {
                        hideChildren(comment);

                    } else {
                        for (BlogComment comment: BlogCommentView.this.comment.getChildren(MySaasaApplication.getService().getCommentManager())) {
                            comment.client_visible = true;
                        }
                    }
                    notifyChildVisibilityChanged();
                }
            });
            if (comment.getChildren(MySaasaApplication.getService().getCommentManager()).get(0).client_visible) {
                showChildren.setText(TREE_CLOSE);
            } else {
                showChildren.setText(TREE_OPEN);
            }

        }
    }

    public void hideChildren(BlogComment comment) {
        for (BlogComment comment1: comment.getChildren(MySaasaApplication.getService().getCommentManager())) {
            comment1.client_visible = false;
            hideChildren(comment1);
        }
    }

    protected abstract void notifyChildVisibilityChanged();

    public void setHighlight(boolean b) {
//        if (b == true) setBackgroundColor(Color.argb(25,255,255,255));
    }
}
