package com.mysaasa.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysassa.R;
import com.mysaasa.api.model.Category;

/**
 * Created by Adam on 1/2/2015.
 */
public class BlogCategoryView extends FrameLayout {
    Category category;

    TextView title;

    TextView count;

    public BlogCategoryView(Context context) {
        super(context);
        init();
    }

    public BlogCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlogCategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.listitem_blogcategoryview,this);
        title = (TextView) findViewById(R.id.title);
        count = (TextView) findViewById(R.id.count);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        title.setText(category.name);
        count.setVisibility(View.GONE);

    }
}
