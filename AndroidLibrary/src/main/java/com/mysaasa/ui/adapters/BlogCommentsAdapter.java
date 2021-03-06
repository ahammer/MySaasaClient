package com.mysaasa.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mysaasa.ui.views.BlogCommentView;
import com.mysaasa.api.model.BlogComment;
import com.mysaasa.api.model.BlogPost;

import java.util.List;

/**
 * Created by Adam on 3/20/2016.
 */
public class BlogCommentsAdapter extends BaseAdapter {
    private final List<BlogComment> list;
    private final Long selected_comment_id;
    private final BlogPost post;

    public BlogCommentsAdapter(List<BlogComment> list, Long selected_comment_id, BlogPost post) {
        this.list = list;
        this.selected_comment_id = selected_comment_id;
        this.post = post;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BlogCommentView bcv = new BlogCommentView(viewGroup.getContext(), post, list.get(i));

        bcv.setComment(list.get(i));
        if (list.get(i).getId() == selected_comment_id) {
            bcv.setHighlight(true);
        } else {
            bcv.setHighlight(false);
        }

        return bcv;
    }
}
