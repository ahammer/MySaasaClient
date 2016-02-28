package com.adamhammer;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mysassa.api.model.BlogPost;
import com.mysassa.whitelabel.R;

import java.util.List;

/**
 * Created by Adam on 2/27/2016.
 */
class MyBlogPostsAdapter extends BaseAdapter {
    final List<BlogPost> blogPosts;

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
        TextView tv = new TextView(viewGroup.getContext());
        tv.setText(Html.fromHtml(blogPosts.get(i).title + "<br/>" + blogPosts.get(i).body));
        tv.setBackgroundResource(R.drawable.dark_panel);
        tv.setPadding(50, 50, 50, 50);
        return tv;
    }
}
