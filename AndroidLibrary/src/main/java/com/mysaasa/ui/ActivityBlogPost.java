package com.mysaasa.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ApplicationSectionsManager;

import com.mysassa.R;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.model.User;
import com.mysaasa.ui.fragments.BlogCommentViewer;

import java.io.Serializable;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Adam on 12/30/2014.
 */
public class ActivityBlogPost extends SideNavigationCompatibleActivity {
    private State state = new State();
    TextView body;
    ViewGroup bodyContainer;
    BlogCommentViewer viewer;
    long selected_comment_id = 0;

    public static void startActivity(Context ctx, BlogPost post, Category c) {
        Intent intent = new Intent(ctx, ActivityBlogPost.class);
        intent.putExtra("post",post);
        intent.putExtra("category",c);
        ctx.startActivity(intent);
    }


    enum States {BlogOnly, CommentsOnly, Both}
    States currentState = States.Both;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);
        selected_comment_id = getIntent().getLongExtra("selected_comment_id",0);
        initializeSideNav();
        if (savedInstanceState != null && savedInstanceState.containsKey("state")) {
            state = (State) savedInstanceState.getSerializable("state");
        } else {
            state.post = (BlogPost) getIntent().getSerializableExtra("post");
        }

        if (selected_comment_id != 0) currentState = States.CommentsOnly;
        bodyContainer = (ViewGroup) findViewById(R.id.body_container);
        body = (TextView) findViewById(R.id.body);
        handleVisibility();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state", state);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200202 && resultCode == Activity.RESULT_OK) {
            //viewer.refresh();
            if (data != null && data.hasExtra("post")) {
                state.post = (BlogPost) data.getSerializableExtra("post");
                handleVisibility();
            } else {

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_article, menu);
        ApplicationSectionsManager.CategoryDef def;
        try {
            def = MySaasaApplication.getInstance().getAndroidCategoryManager().getCategoryDef((Category) getIntent().getSerializableExtra("category"));
        } catch (Exception e) {
            def = new ApplicationSectionsManager.CategoryDef();
        }
        if (def.commentsAllowed) {

            menu.findItem(R.id.action_comment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ActivityPostComment.postComment(ActivityBlogPost.this, state.post, null);
                    return true;
                }
            });
            menu.findItem(R.id.action_switch_layout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (currentState) {
                        case BlogOnly:
                            currentState = States.CommentsOnly;
                            break;
                        case CommentsOnly:
                            currentState = States.Both;
                            break;
                        case Both:
                            currentState=States.BlogOnly;
                            break;
                    }
                    handleVisibility();
                    return true;
                }
            });

            //User u = MySaasaApplication.getService().get().user;
            User u = null;

            MenuItem delete = menu.findItem(R.id.action_delete);
            MenuItem edit = menu.findItem(R.id.action_edit);
            if (u == null || u.id != state.post.author.id) {
                delete.setVisible(false);
                edit.setVisible(false);
            } else {
                delete.setVisible(true);
                delete.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        new AlertDialog.Builder(ActivityBlogPost.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Delete Post")
                                .setMessage("Are you sure you want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        finish();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();
                        Crouton.makeText(ActivityBlogPost.this, "Delete post", Style.INFO).show();
                        return true;
                    }
                });

                edit.setVisible(true);
                edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ActivityPostToBlog.editComment(ActivityBlogPost.this, state.post);
                        return true;
                    }
                });
            }


        } else {
            menu.findItem(R.id.action_comment).setVisible(false);
            menu.findItem(R.id.action_switch_layout).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }



    private void handleVisibility() {
        if (state.post == null) return;

        if (state.post != null && state.post.body != null) {
            body.setText(Html.fromHtml(state.post.body));
        } else {
            body.setText("No Body");
        }

        body.setMovementMethod(LinkMovementMethod.getInstance());
        setTitle(state.post.title);
        getActionBar().setDisplayShowTitleEnabled(false);
        viewer = (BlogCommentViewer) getFragmentManager().findFragmentById(R.id.blog_comments);
        viewer.setSelected_comment_id(selected_comment_id);
        viewer.setPost(state.post);

        ApplicationSectionsManager.CategoryDef def = MySaasaApplication.getInstance().getAndroidCategoryManager().getCategoryDef((Category) getIntent().getSerializableExtra("category"));
        if (def.commentsAllowed) {
            switch (currentState) {
                case BlogOnly:
                    bodyContainer.setVisibility(View.VISIBLE);
                    viewer.getView().setVisibility(View.GONE);
                    break;
                case CommentsOnly:
                    bodyContainer.setVisibility(View.GONE);
                    viewer.getView().setVisibility(View.VISIBLE);
                    break;
                case Both:
                    bodyContainer.setVisibility(View.VISIBLE);
                    viewer.getView().setVisibility(View.VISIBLE);
                    break;

            }

        } else {
            viewer.getView().setVisibility(View.GONE);
        }

    }

    static class State implements Serializable {
        private BlogPost post;
    }
}
