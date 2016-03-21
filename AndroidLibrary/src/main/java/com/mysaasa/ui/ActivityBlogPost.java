package com.mysaasa.ui;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ApplicationSectionsManager;

import com.mysaasa.ui.fragments.BlogCommentsViewer;
import com.mysassa.R;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.model.User;

import java.io.Serializable;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Adam on 12/30/2014.
 */
public class ActivityBlogPost extends SideNavigationCompatibleActivity {
    private State state = new State();
    TextView body;
    ViewGroup commentContainer;
    ViewGroup bodyContainer;
    BlogCommentsViewer blogCommentsViewer;
    FloatingActionButton floatingActionButtonComments;
    long selected_comment_id = 0;
    private ValueAnimator animator;

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
        commentContainer = (ViewGroup) findViewById(R.id.comment_container);
        bodyContainer = (ViewGroup) findViewById(R.id.body_container);
        body = (TextView) findViewById(R.id.body);
        floatingActionButtonComments = (FloatingActionButton) findViewById(R.id.comments_fab);
        floatingActionButtonComments.setOnClickListener(view -> {
            toggleComments();
        });
        refreshAll();
    }

    private void toggleComments() {
        if (animator == null || !animator.isRunning()) {
            if (commentContainer.getVisibility() == View.GONE) {
                animateCommentDrawer(0, 1);
            } else {
                animateCommentDrawer(1, 0);
            }
        }
    }

    private void animateCommentDrawer(int start, int finish) {
        animator = ValueAnimator.ofFloat((float) start, (float) finish);
        final float width =  findViewById(android.R.id.content).getWidth();
        final float height = findViewById(android.R.id.content).getHeight();

        ViewGroup.LayoutParams lpa = blogCommentsViewer.getView().getLayoutParams();
        lpa.width= (int) ((width));
        lpa.height= (int) ((height)/2);
        blogCommentsViewer.getView().setLayoutParams(lpa);

        animator.addUpdateListener(animation -> {
            Float f = (Float) animation.getAnimatedValue();
            ViewGroup.LayoutParams lp = commentContainer.getLayoutParams();
            lp.width= (int) ((width));
            lp.height= (int) ((height)*f*.5);
            commentContainer.setLayoutParams(lp);
            if (f == 0) {
                commentContainer.setVisibility(View.GONE);
            } else {
                commentContainer.setVisibility(View.VISIBLE);
            }
            ViewGroup.LayoutParams lpBody = bodyContainer.getLayoutParams();
            lpBody.height = (int) (height - lp.height - 50);
            bodyContainer.setLayoutParams(lpBody);
        });

        animator.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state", state);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshAll();
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

            menu.findItem(R.id.action_comment).setOnMenuItemClickListener(menuItem -> {
                ActivityPostComment.postComment(ActivityBlogPost.this, state.post, null);
                return true;
            });

            User u = null;
            MenuItem delete = menu.findItem(R.id.action_delete);
            MenuItem edit = menu.findItem(R.id.action_edit);
            if (u == null || u.id != state.post.author.id) {
                delete.setVisible(false);
                edit.setVisible(false);
            } else {
                delete.setVisible(true);
                delete.setOnMenuItemClickListener(menuItem -> {
                    new AlertDialog.Builder(ActivityBlogPost.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Post")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                finish();
                            })
                            .setNegativeButton("No", null)
                            .show();
                    Crouton.makeText(ActivityBlogPost.this, "Delete post", Style.INFO).show();
                    return true;
                });

                edit.setVisible(true);
                edit.setOnMenuItemClickListener(menuItem -> {
                    ActivityPostToBlog.editComment(ActivityBlogPost.this, state.post);
                    return true;
                });
            }


        } else {
            menu.findItem(R.id.action_comment).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }



    private void refreshAll() {
        if (state.post == null) return;

        if (state.post != null && state.post.body != null) {
            body.setText(Html.fromHtml(state.post.body));
        } else {
            body.setText("No Body");
        }

        body.setMovementMethod(LinkMovementMethod.getInstance());
        setTitle(state.post.title);
        blogCommentsViewer = (BlogCommentsViewer) getFragmentManager().findFragmentById(R.id.blog_comments);
        blogCommentsViewer.setselectedCommentId(selected_comment_id);
        blogCommentsViewer.setPost(state.post);
    }

    static class State implements Serializable {
        private BlogPost post;
    }
}
