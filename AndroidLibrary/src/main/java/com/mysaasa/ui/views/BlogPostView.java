package com.mysaasa.ui.views;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mysaasa.api.model.BlogPost;
import com.mysassa.R;

/**
 * Created by Adam on 5/31/2016.
 */
public class BlogPostView extends ModelFrameLayout<BlogPost>{
    TextView title;
    TextView author;
    TextView body;

    public BlogPostView(Context context) {
        super(R.layout.view_blogpost, context);
    }

    public BlogPostView(Context context, AttributeSet attrs) {
        super(R.layout.view_blogpost, context, attrs);
    }

    public BlogPostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(R.layout.view_blogpost, context, attrs, defStyleAttr);
    }

    @Override
    protected void bindViews() {
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        body = (TextView) findViewById(R.id.body);
    }

    @Override
    protected void clearViews() {
        setVisibility(GONE);
    }

    @Override
    protected void populateViews() {
        setVisibility(VISIBLE);
        title.setText(getModel().title);
        author.setText(getModel().author.identifier);
        body.setText(Html.fromHtml(getModel().body));
        body.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
