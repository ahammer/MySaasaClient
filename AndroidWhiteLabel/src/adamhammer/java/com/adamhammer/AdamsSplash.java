package com.adamhammer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysaasa.MySaasaApplication;

import com.mysaasa.ui.views.ContactView;
import com.mysaasa.api.model.BlogPost;
import com.mysaasa.api.model.Category;
import com.mysaasa.whitelabel.R;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 1/12/2015.
 */
public class AdamsSplash extends Fragment {
    @BindView(R.id.my_list)
    ScrollSyncListView myList;

    @BindView(R.id.loading_progressbar)
    IntroView loadingProgressbar;

    private ScheduledExecutorService executor;

    private List<BlogPost> blogPosts;
    private Subscription subscription;
    private HeaderView headerView;
    private ContactView contactView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.adam_splash,null);
        ButterKnife.bind(this, vg);
        updateList();
        return vg;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDataFromNetwork();
        startAnimationThread();
    }

    @Override
    public void onStop(){
        super.onStop();
        stopAnimtationThread();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    /**
     * The animation of the Images in the scroll list
     */
    private void startAnimationThread() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate((Runnable) () -> getActivity().runOnUiThread(() -> {
            updateListImages();
        }), 0, (int) (1 / 60f * 1000), TimeUnit.MILLISECONDS);
    }

    private void updateListImages() {
        for (int i=0;i<myList.getChildCount();i++) {
            View v = myList.getChildAt(i);
            if (v instanceof ZoomingSpaceImageView) {
                ZoomingSpaceImageView iv = (ZoomingSpaceImageView) v;
                iv.setCurrentTransition(v.getY()/myList.getHeight());
            }
        }
    }

    private void stopAnimtationThread() {
        executor.shutdownNow();
    }

    private void loadDataFromNetwork() {
        Category category = new Category("Resume");
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = MySaasaApplication.getService().getBlogManager().getBlogPostsObservable(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toSortedList((blogPost, blogPost2) -> blogPost.priority - blogPost2.priority)
                .subscribe(blogPosts1 -> {
                    AdamsSplash.this.setBlogPosts(blogPosts1);
                });
    }

    public void setBlogPosts(List<BlogPost> blogPosts) {
        AdamsSplash.this.blogPosts = blogPosts;
        updateList();
    }


    private void updateList() {
        myList.setDividerHeight(0);
        loadingProgressbar.setVisibility(blogPosts==null?View.VISIBLE:View.GONE);
        if (blogPosts == null || blogPosts.size() == 0) return;
        myList.setAdapter(new JoiningAdapter(new MyPhotosAdapter(), new MyBlogPostsAdapter(blogPosts)));
        if (headerView != null) myList.removeHeaderView(headerView);
        myList.addHeaderView(headerView = new HeaderView(getActivity()));
        if (contactView != null) myList.removeFooterView(contactView);
        myList.addFooterView(contactView = new ContactView(getActivity()));
        contactView.setToUser("admin");

    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }


}
