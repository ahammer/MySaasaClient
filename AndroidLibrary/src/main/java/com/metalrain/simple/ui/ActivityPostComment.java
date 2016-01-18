package com.metalrain.simple.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.metalrain.simple.R;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.model.BlogComment;
import com.metalrain.simple.api.model.BlogPost;

import java.io.Serializable;

/**
 * Created by administrator on 2014-06-30.
 */
public class ActivityPostComment extends Activity {
    boolean editMode = false;

    static class State implements Serializable {
        BlogPost post = null;
        BlogComment comment = null;

    }


    State state = new State();

    TextView title, reply;
    EditText commentBox;
    Button postButton;
    public static void editComment(Activity ctx, BlogComment comment) {
        Intent i = new Intent(ctx, ActivityPostComment.class);
        i.putExtra("comment",comment);
        i.putExtra("edit_mode",true);
        ctx.startActivityForResult(i, 200202);
    }

    public static void postComment(Activity ctx, BlogPost post, BlogComment comment) {
        Intent i = new Intent(ctx, ActivityPostComment.class);
        i.putExtra("edit_mode",false);
        i.putExtra("post",post);
        i.putExtra("comment",comment);
        ctx.startActivityForResult(i, 200202);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMode = getIntent().getBooleanExtra("edit_mode",false);
        if (editMode) setTitle("Edit Comment");
        setContentView(R.layout.activity_post_comment);
        if (savedInstanceState != null && savedInstanceState.containsKey("state")) {
            state = (State)savedInstanceState.getSerializable("state");
        } else {
            state.post = (BlogPost) getIntent().getSerializableExtra("post");
            state.comment = (BlogComment) getIntent().getSerializableExtra("comment");
        }

        title = (TextView) findViewById(R.id.title);
        reply = (TextView) findViewById(R.id.reply);
        commentBox = (EditText) findViewById(R.id.comment);
        postButton = (Button) findViewById(R.id.post);


        if (state.comment != null) {
            reply.setText(state.comment.author.identifier + " said \""+state.comment.content+"\"");
        } else {
            reply.setVisibility(View.GONE);
        }
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                        SimpleApplication.getService().updateBlogComment(state.comment, commentBox.getText().toString());
                } else {
                    if (state.comment == null) {
                        SimpleApplication.getService().commentOnBlog(state.post.id, commentBox.getText().toString());
                    } else {
                        SimpleApplication.getService().replyToComment(state.comment.id, commentBox.getText().toString());
                    }
                }
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        if (!SimpleApplication.getService().getState().authenticated) {

            Intent i = new Intent(this, ActivitySignin.class);
            startActivityForResult(i, 10010);
        }
        if (editMode) {
            reply.setVisibility(View.GONE);
            postButton.setText("Save Post");
            title.setVisibility(View.GONE);
            commentBox.setText(state.comment.content);
        } else {
            if (state.post != null && state.post.title != null) {
                title.setText(state.post.title);
            } else {
                title.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state",state);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010 && resultCode != Activity.RESULT_OK) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

}
