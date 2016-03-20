package com.mysaasa.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mysaasa.MySaasaApplication;
import com.mysassa.R;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.responses.PostCommentResponse;
import com.mysassa.api.responses.PostReplyResponse;

import java.io.Serializable;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by administrator on 2014-06-30.
 */
public class ActivityPostComment extends Activity {
    TextView title, reply;
    EditText commentBox;
    Button postButton;

    private boolean editMode = false;
    private State state = new State();

    static class State implements Serializable {
        BlogPost post = null;
        BlogComment comment = null;
    }


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
            reply.setText(state.comment.getAuthor().identifier + " said \""+ state.comment.getContent() +"\"");
        } else {
            reply.setVisibility(View.GONE);
        }
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    System.out.println(state);
                } else {
                    if (state.post != null) {
                        MySaasaApplication
                                .getService()
                                .getCommentManager()
                                .postBlogComment(state.post, commentBox.getText().toString())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Crouton.makeText(ActivityPostComment.this, throwable.getMessage(), Style.ALERT).show();
                                    }
                                })
                                .subscribe(new Action1<PostCommentResponse>() {
                                    @Override
                                    public void call(PostCommentResponse postCommentResponse) {
                                        if (postCommentResponse.isSuccess()) {
                                            setResult(Activity.RESULT_OK);
                                            finish();
                                        } else {
                                            Crouton.makeText(ActivityPostComment.this, postCommentResponse.getMessage(), Style.ALERT).show();
                                        }
                                    }

                                });
                    } else if (state.comment != null) {
                        MySaasaApplication
                                .getService()
                                .getCommentManager()
                                .postCommentResponse(state.comment, commentBox.getText().toString())
                                .onBackpressureBuffer()
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Crouton.makeText(ActivityPostComment.this, throwable.getMessage(), Style.ALERT).show();
                                    }
                                })
                                .subscribe(new Action1<PostReplyResponse>() {
                                    @Override
                                    public void call(PostReplyResponse postReplyResponse) {
                                        if (postReplyResponse.isSuccess()) {
                                            setResult(Activity.RESULT_OK);
                                            finish();
                                        } else {
                                            Crouton.makeText(ActivityPostComment.this, postReplyResponse.getMessage(), Style.ALERT).show();
                                        }
                                    }

                                });

                    }

                }
            }
        });


        if (MySaasaApplication.getService().getLoginManager().getAuthenticatedUser() == null) {
            Intent i = new Intent(this, ActivitySignin.class);
            startActivityForResult(i, 10010);
        }

        if (editMode) {
            reply.setVisibility(View.GONE);
            postButton.setText("Save Post");
            title.setVisibility(View.GONE);
            commentBox.setText(state.comment.getContent());
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
