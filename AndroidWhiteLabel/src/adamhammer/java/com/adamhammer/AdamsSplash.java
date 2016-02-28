package com.adamhammer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.mysassa.SimpleApplication;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.whitelabel.R;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 1/12/2015.
 */
public class AdamsSplash extends Activity {

    @Bind(R.id.background_image)
    ZoomingSpaceImageView backgroundImage;

    @Bind(R.id.my_list)
    ScrollSyncListView myList;

    @Bind(R.id.loading_progressbar)
    IntroView loadingProgressbar;
    private ScheduledExecutorService executor;

    private static List<BlogPost> blogPosts;

    public void setBlogPosts(List<BlogPost> blogPosts) {
        AdamsSplash.this.blogPosts = blogPosts;
        updateBlogList();
    }

    public static List<BlogPost> getBlogPosts() {
        return blogPosts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adam_splash);
        ButterKnife.bind(this);
        backgroundImage.setCurrentTransition(0);
        updateBlogList();
        executor = Executors.newScheduledThreadPool(1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromNetwork();
        startAnimationThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimtationThread();
    }

    private void startAnimationThread() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        backgroundImage.setCurrentTransition(myList.getScrollYPercentage());
                    }
                });
            }
        }, 0, (int) (1 / 60f * 1000), TimeUnit.MILLISECONDS);
    }

    private void stopAnimtationThread() {
        executor.shutdownNow();
    }

    private void loadDataFromNetwork() {
        Category category = new Category("Resume");
        SimpleApplication.getService().getBlogPostsObservable(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Action1<List<BlogPost>>() {
                    @Override
                    public void call(List<BlogPost> blogPosts) {
                        AdamsSplash.this.setBlogPosts(blogPosts);
                    }
                });
    }

    private void updateBlogList() {
        loadingProgressbar.setVisibility(blogPosts==null?View.VISIBLE:View.GONE);
        if (blogPosts == null || blogPosts.size() == 0) return;
        myList.setAdapter(new MyBlogPostsAdapter(blogPosts));
        myList.setDivider(null);
        myList.setDividerHeight(50);
    }


}
