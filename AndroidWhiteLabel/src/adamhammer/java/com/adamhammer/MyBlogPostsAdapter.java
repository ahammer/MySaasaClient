package com.adamhammer;

import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mysaasa.api.model.BlogPost;
import com.mysaasa.ui.views.BlogPostListItem;
import com.mysaasa.ui.views.BlogPostView;
import com.mysaasa.whitelabel.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Adam on 2/27/2016.
 */
class MyBlogPostsAdapter extends BaseAdapter {
    final List<BlogPost> blogPosts;
    HashMap<BlogPost, Spanned> mCachedText = new HashMap<BlogPost, Spanned>();

    MyBlogPostsAdapter(List<BlogPost> blogPosts) {
        this.blogPosts = blogPosts;
    }

    @Override
    public int getCount() {
        if (blogPosts == null) return 0;
        return blogPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return blogPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return blogPosts.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BlogPostView bpli = null;
        if (view instanceof BlogPostListItem) {
            bpli = (BlogPostView) view;
        }

        if (bpli == null) {
            bpli = new BlogPostView(viewGroup.getContext());
        }
        bpli.setModel((BlogPost) getItem(i));
        return bpli;
    }
}
