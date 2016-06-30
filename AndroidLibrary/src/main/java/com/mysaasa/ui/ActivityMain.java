package com.mysaasa.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;


import com.mysaasa.MySaasaApplication;
import com.mysaasa.ApplicationSectionsManager;
import com.mysassa.R;
import com.mysaasa.api.model.BlogPost;
import com.mysaasa.api.model.Category;
import com.mysaasa.ui.adapters.BlogAdapter;
import com.mysaasa.ui.fragments.EmptyListAdapter;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This is the launch point for the applicatoin, the root activity.
 *
 * There is a sidebar and fragment support in the main panel. Other specialized activities
 * might be launched from it. It supports top level browsing of the app.
 *
 * Created by adam on 2014-10-31.
 */
public class ActivityMain extends SideNavigationCompatibleActivity {
    private ListView newsList;

    private MenuItem post;
    private FrameLayout fragmentFrame;
    private MenuItem cart;
    private Subscription subscription;
    private List<BlogPost> posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isConnected()) {
            return;
        }
        setContentView(R.layout.activity_main);

        initializeSideNav();

        if (getIntent().getSerializableExtra("category") != null) {
            selectedCategory = (Category) getIntent().getSerializableExtra("category");
        }
        if (savedInstanceState != null && savedInstanceState.get("category") != null) {
            selectedCategory = (Category) savedInstanceState.getSerializable("category");
        }

        fragmentFrame = (FrameLayout) findViewById(R.id.fragment_frame);
        newsList = (ListView) findViewById(R.id.content_frame);
        newsList.setOnItemClickListener((adapterView, view, i, l) -> {
            Object o = adapterView.getAdapter().getItem(i);
            if (o instanceof BlogPost) {
                ActivityBlogPost.startActivity(ActivityMain.this, (BlogPost) o, selectedCategory);
            }
        });

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("category", selectedCategory);
    }

    @Override
    protected void onPause() {
        if (subscription != null) subscription.unsubscribe();
        super.onPause();
    }

    @Override
    protected void categoryChanged(Category c) {
        super.selectedCategory = c;
        updateBlogList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.standard,menu);
        post = menu.findItem(R.id.action_post);

        if (post != null) {
            post.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ActivityPostToBlog.postComment(ActivityMain.this, selectedCategory);
                    return true;
                }
            });

        }

        updateBlogList();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            updateBlogPostSubscription();
        }
    }

    @Override
    public void onBackPressed() {
        if (isSidenavOpen()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            mDrawerLayout.openDrawer(sidenav);
        }
    }
    private void updateBlogPostSubscription() {
        if (subscription != null) subscription.unsubscribe();
        ApplicationSectionsManager.CategoryDef categoryDef = MySaasaApplication.getInstance().getAndroidCategoryManager().getCategoryDef(selectedCategory);

        subscription = MySaasaApplication.getService()
                .getBlogManager()
                .getBlogPostsObservable(getSelectedCategory())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toSortedList((blogPost, blogPost2) -> {
                    return Integer.valueOf((int) (blogPost2.id - blogPost.id));
                }).subscribe(this::setPosts, this::handleError);
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        Crouton.makeText(this, throwable.getMessage(), Style.ALERT).show();
    }

    public void setPosts(List<BlogPost> posts) {
        this.posts = posts;
        newsList.setAdapter(new BlogAdapter(posts));
    }




    protected void updateBlogList() {
        if (!isConnected()) return;
        ApplicationSectionsManager.CategoryDef def = MySaasaApplication.getInstance().getAndroidCategoryManager().getCategoryDef(selectedCategory);

        setTitle(def.title);
        if (TextUtils.isEmpty(def.fragment)) {
            newsList.setVisibility(View.VISIBLE);
            fragmentFrame.setVisibility(View.GONE);
            updateBlogPostSubscription();
            if (post != null) {
                post.setVisible(def.postsAllowed);
            }
        } else {
            post.setVisible(false);
            newsList.setAdapter(new EmptyListAdapter() {
                @Override
                protected void Clicked() {

                }

                @Override
                protected String getText() {
                    return "--- No News ---";
                }
            });
            fragmentFrame.setVisibility(View.VISIBLE);
            newsList.setVisibility(View.GONE);
            try {
                Fragment f = (Fragment) Class.forName(def.fragment).newInstance();
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame, f).commit();
            } catch (Exception e) {}
        }
    }

    public static void startFreshTop(Context ctx, Category c) {
        Intent i = new Intent(ctx,ActivityMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("category", c);
        ctx.startActivity(i);

    }

    public List<BlogPost> getPosts() {
        return posts;
    }
}
