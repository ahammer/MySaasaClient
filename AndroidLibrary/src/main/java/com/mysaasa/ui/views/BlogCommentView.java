package com.mysaasa.ui.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.R;
import com.mysaasa.api.model.BlogComment;
import com.mysaasa.api.model.BlogPost;
import com.mysaasa.api.model.User;
import com.mysaasa.ui.ActivityPostComment;

import java.util.List;

import static com.mysaasa.api.Preconditions.checkNotNull;

/**
 * Created by Adam on 1/6/2015.
 */
public class BlogCommentView extends FrameLayout {
    CardView cardView;
    BlogComment comment;
    BlogPost post;
    private TextView body;
    private TextView author;
    private ImageView remove, vote, reply, edit;
    private LinearLayout children;



    public BlogCommentView(Context context, BlogPost post, BlogComment comment) {
        super(context);
        init();
        this.post = post;
        setComment(comment);
    }


    private void init() {
        inflate(getContext(),R.layout.listitem_blogcomment,this);
        author = (TextView)findViewById(R.id.author);
        body = (TextView)findViewById(R.id.content);
        remove = (ImageView) findViewById(R.id.remove);
        vote = (ImageView) findViewById(R.id.vote);
        reply = (ImageView) findViewById(R.id.reply);
        edit = (ImageView) findViewById(R.id.edit);
        children = (LinearLayout) findViewById(R.id.children);
        cardView = (CardView) findViewById(R.id.cardview);
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

    public void setComment(final BlogComment comment) {
        this.comment = comment;
        if (comment.getAuthor() !=null) {
            author.setText("Posted by " + comment.getAuthor().identifier + " on " + comment.getDateCreated());
        }else  {
            author.setText("");
        }

        body.setText(comment.getContent());
        cardView.setCardElevation(20+comment.calculateDepth(MySaasaApplication.getService().getCommentManager()));
        handleButtonVisibilities(comment);

        List<BlogComment> blogComments = comment.getChildren(MySaasaApplication.getService().getCommentManager());
        children.removeAllViews();
        for (final BlogComment blogComment:blogComments) {
            BlogCommentView bcv = new BlogCommentView(getContext(),post,blogComment);
            children.addView(bcv);
        }


    }

    private void handleButtonVisibilities(BlogComment comment) {

        User u = MySaasaApplication.getService().getAuthenticationManager().getAuthenticatedUser();
        if (comment.getAuthor() == null) {                       //Only Deleted comments have null author
            remove.setVisibility(View.GONE);
            vote.setVisibility(View.GONE);
            reply.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        } else if (u!=null && u.id == comment.getAuthor().id){   //You are the author
            //remove.setVisibility(View.VISIBLE);
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
    }


    public void setHighlight(boolean b) {
//        if (b == true) setBackgroundColor(Color.argb(25,255,255,255));
    }
}
