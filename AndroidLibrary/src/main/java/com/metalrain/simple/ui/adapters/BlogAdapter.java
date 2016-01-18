package com.metalrain.simple.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metalrain.simple.AndroidCategoryManager;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.model.BlogPost;
import com.metalrain.simple.ui.ActivityMain;
import com.metalrain.simple.ui.views.BlogPostListItem;
import com.metalrain.simple.ui.views.EmptyListItem;

import java.util.Collections;
import java.util.List;

/**
* Created by Adam on 1/4/2015.
*/
public class BlogAdapter extends BaseAdapter {
    List<BlogPost> posts;
    AndroidCategoryManager.CategoryDef categoryDef;

    public BlogAdapter(ActivityMain blogCategoryActivity) {
        categoryDef = SimpleApplication.getInstance().getAndroidCategoryManager().getCategoryDef(blogCategoryActivity.selectedCategory);
        posts = getBlogPosts();
    }

    public List<BlogPost> getBlogPosts() {
        return SimpleApplication.getService().getBlogPosts(categoryDef.toCategory(), 0, categoryDef.pagesize, categoryDef.sortColumn, categoryDef.sortOrder);
    }


    @Override
    public void notifyDataSetChanged() {
        posts = SimpleApplication.getService().mBlogPostManager.getBlogPostsForCategory(categoryDef.toCategory());
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (posts == null || posts.size() == 0) {
            return 1;
        }
        try {
            return posts.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (posts.size() == 0) return null;
        return posts.get(i);

    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (posts == null || posts == Collections.EMPTY_LIST) {
            TextView empty = new TextView(viewGroup.getContext());
            empty.setText("Loading please wait");
            return  empty;
        } else if (posts.size() == 0) {
            EmptyListItem empty = new EmptyListItem(viewGroup.getContext());
            empty.setText("--- No Posts ---");
            return  empty;
        }
        if (view instanceof BlogPostListItem) {
            BlogPostListItem bpv = (BlogPostListItem) view;
            bpv.setBlogPost((BlogPost) getItem(i));
            return bpv;
        }
        BlogPostListItem bpv = new BlogPostListItem(viewGroup.getContext());
        BlogPost blogPost = (BlogPost) getItem(i);
        bpv.setBlogPost(blogPost);
        return bpv;
    }
}
