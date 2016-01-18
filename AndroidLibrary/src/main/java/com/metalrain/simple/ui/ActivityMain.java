package com.metalrain.simple.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.metalrain.simple.AndroidCategoryManager;
import com.metalrain.simple.R;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.messages.BlogPostsModified;
import com.metalrain.simple.api.messages.SigninMessage;
import com.metalrain.simple.api.model.BlogPost;
import com.metalrain.simple.api.model.Category;
import com.metalrain.simple.ui.adapters.BlogAdapter;
import com.metalrain.simple.ui.fragments.EmptyListAdapter;

/**
 * This is the launch point for the applicatoin, the root activity.
 *
 * There is a sidebar and fragment support in the main panel. Other specialized activities
 * might be launched from it. It supports top level browsing of the app.
 *
 * Created by adam on 2014-10-31.
 */
public class ActivityMain extends ActivityBase {
    //ListView navList;
    private ListView newsList;
    private TextView title;
    private MenuItem post;
    private MenuItem refresh;
    private FrameLayout fragmentFrame;
    private MenuItem cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSideNav();

        if (getIntent().getSerializableExtra("category") != null) {
            selectedCategory = (Category) getIntent().getSerializableExtra("category");
        }

        fragmentFrame = (FrameLayout) findViewById(R.id.fragment_frame);
        newsList = (ListView) findViewById(R.id.content_frame);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = adapterView.getAdapter().getItem(i);
                if (o instanceof BlogPost) {
                    ActivityBlogPost.startActivity(ActivityMain.this, (BlogPost) o, selectedCategory);
                }
            }
        });

        title = (TextView)findViewById(R.id.title);
        if (savedInstanceState != null && savedInstanceState.get("category") != null) {
            selectedCategory = (Category) savedInstanceState.getSerializable("category");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("category", selectedCategory);
    }

    @Override
    protected void categoryChanged(Category c) {
        updateNewsList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.standard,menu);
        post = menu.findItem(R.id.action_post);
        refresh = menu.findItem(R.id.action_refresh);


        if (post != null) {
            post.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ActivityPostToBlog.postComment(ActivityMain.this, selectedCategory);
                    return true;
                }
            });

        }

        if (refresh != null) {

            refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    updateNewsList();
                    return true;
                }
            });
        }

        updateNewsList();
        return super.onCreateOptionsMenu(menu);
    }

    protected void updateNewsList() {
        AndroidCategoryManager.CategoryDef def =SimpleApplication.getInstance().getAndroidCategoryManager().getCategoryDef(selectedCategory);
        title.setText(def.title);

        if (TextUtils.isEmpty(def.fragment)) {
            newsList.setVisibility(View.VISIBLE);
            fragmentFrame.setVisibility(View.GONE);
            newsList.setAdapter(new BlogAdapter(this));
            title.setVisibility(View.VISIBLE);
            if (post != null) {
                post.setVisible(def.postsAllowed);
                refresh.setVisible(true);
            }


        } else {
            refresh.setVisible(false);
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
            title.setVisibility(View.GONE);
            fragmentFrame.setVisibility(View.VISIBLE);
            newsList.setVisibility(View.GONE);
            try {
                Fragment f = (Fragment) Class.forName(def.fragment).newInstance();
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame, f).commit();
            } catch (Exception e) {}


        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityPostToBlog.REQUEST_CODE && RESULT_OK == resultCode) {
            Adapter a = newsList.getAdapter();
            if (a instanceof BlogAdapter) {
                BlogAdapter ba = (BlogAdapter) a;
                ba.getBlogPosts();
            }
        }
    }

    @Override
    protected void handleMessage(Object o) {
        super.handleMessage(o);
        if (o instanceof  SigninMessage) {
            onLoginResult((SigninMessage)o);
        } else if (o instanceof BlogPostsModified) {
            blogPostsUpdated((BlogPostsModified)o);

        }
    }


    public void onLoginResult(final SigninMessage m) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (m.isSuccess()) mDrawerLayout.closeDrawers();
            }
        });

    }


    public void blogPostsUpdated(BlogPostsModified message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseAdapter adapter = (BaseAdapter) newsList.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static void startFreshTop(Context ctx, Category c) {
        Intent i = new Intent(ctx,ActivityMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("category", c);
        ctx.startActivity(i);

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
}
