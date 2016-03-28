package com.adamhammer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import com.mysaasa.MySaasaApplication;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.whitelabel.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 1/12/2015.
 */
public class AdamsSplash extends Fragment {
    @Bind(R.id.background_image)
    ZoomingSpaceImageView backgroundImage;

    @Bind(R.id.my_list)
    ScrollSyncListView myList;

    @Bind(R.id.loading_progressbar)
    IntroView loadingProgressbar;

    private ScheduledExecutorService executor;

    private List<BlogPost> blogPosts;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.adam_splash,null);
        ButterKnife.bind(this, vg);
        backgroundImage.setCurrentTransition(0);
        myList.setDivider(null);
        myList.setDividerHeight(0);
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
    }

    private void startAnimationThread() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate((Runnable) () -> getActivity().runOnUiThread(() -> {

            backgroundImage.setCurrentTransition(myList.getScrollYPercentage());
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
        subscription = MySaasaApplication.getService().getBlogManager().getBlogPostsObservable(category)
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

    public void setBlogPosts(List<BlogPost> blogPosts) {
        AdamsSplash.this.blogPosts = blogPosts;
        updateList();
    }


    private void updateList() {
        loadingProgressbar.setVisibility(blogPosts==null?View.VISIBLE:View.GONE);
        if (blogPosts == null || blogPosts.size() == 0) return;
        //myList.setAdapter();
        myList.setAdapter(new JoiningAdapter(new MyPhotosAdapter(), new MyBlogPostsAdapter(blogPosts)));
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public class JoiningAdapter extends BaseAdapter{
        final ListAdapter mAdapter1;
        final ListAdapter mAdapter2;
        List<Entry> entries = new ArrayList<Entry>();

        class Entry {
            final boolean inAdapter1;
            final int pos;

            Entry(boolean inAdapter1, int pos) {
                this.inAdapter1 = inAdapter1;
                this.pos = pos;
            }
        }

        public JoiningAdapter(ListAdapter a1, ListAdapter a2) {
            this.mAdapter1 = a1;
            this.mAdapter2 = a2;
            init();
        }

        private void init() {
            float ratio = mAdapter1.getCount()/(float)mAdapter2.getCount();

            float count = 0;
            int pos1 = 0;
            int pos2 = 0;
            while (pos1 < mAdapter1.getCount() || pos2 < mAdapter2.getCount()) {
                if (Math.floor(count+ratio) == Math.floor(count)) {
                    if (pos2 >= mAdapter2.getCount()) return;
                    entries.add(new Entry(false,pos2++));
                } else {
                    if (pos1 >= mAdapter1.getCount()) return;
                    entries.add(new Entry(true,pos1++));

                }
                count += ratio;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return entries.get(position).inAdapter1?0:1;
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public Object getItem(int i) {
            return ((entries.get(i).inAdapter1)?mAdapter1:mAdapter2).getItem(entries.get(i).pos);
        }

        @Override
        public long getItemId(int i) {
            return ((entries.get(i).inAdapter1) ? mAdapter1 : mAdapter2).getItemId(entries.get(i).pos);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return ((entries.get(i).inAdapter1) ? mAdapter1 : mAdapter2).getView(entries.get(i).pos,view,viewGroup);
        }
    }


}
