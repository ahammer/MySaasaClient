package com.mysaasa.ui.views;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mysaasa.R;
import com.mysaasa.api.model.BlogPost;
import com.mysaasa.utility.StringUtility;

/**
 * This should be the item that contains a view
 * Created by administrator on 2014-07-01.
 */
public class BlogPostListItem extends FrameLayout {
    private final TextView title;
    private final TextView summary;
    private final ViewGroup baseView;
    private BlogPost blogPost;

    public BlogPostListItem(Context context) {
        super(context);
        baseView = (ViewGroup) inflate(context, R.layout.listitem_blogpost, this);
        title = (TextView) findViewById(R.id.title);
        summary = (TextView) findViewById(R.id.content);
    }

    public void setBlogPost(BlogPost blogPost) {
        if (blogPost == null) return;
        this.blogPost = blogPost;
        title.setText(StringUtility.trimTrailingWhitespace(Html.fromHtml(blogPost.title)));
        if (!TextUtils.isEmpty(blogPost.summary)) {
            summary.setText(StringUtility.trimTrailingWhitespace(Html.fromHtml(blogPost.summary)));
            summary.setVisibility(View.VISIBLE);
        } else {
            summary.setVisibility(View.GONE);
        }

    }
}
