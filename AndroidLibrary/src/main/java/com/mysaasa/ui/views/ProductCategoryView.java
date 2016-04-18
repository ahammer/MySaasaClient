package com.mysaasa.ui.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysassa.R;
import com.mysaasa.api.model.Category;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by adam on 15-02-22.
 */
public class ProductCategoryView extends FrameLayout{
    private final TextView title;
    private Category category;

    public ProductCategoryView(Context context) {
        super(context);
        inflate(context, R.layout.product_category_view, this);
        title = (TextView)findViewById(R.id.title);
    }


    public void setCategory(Category category) {
        checkNotNull(category);
        this.category = category;
        title.setText(category.name);
    }

    public Category getCategory() {
        return category;
    }
}
