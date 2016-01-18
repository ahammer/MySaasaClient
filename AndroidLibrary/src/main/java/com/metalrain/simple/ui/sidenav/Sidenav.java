package com.metalrain.simple.ui.sidenav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.metalrain.simple.AndroidCategoryManager;
import com.metalrain.simple.R;
import com.metalrain.simple.api.messages.BlogCategoriesReceivedMessage;
import com.metalrain.simple.api.model.Category;
import com.metalrain.simple.ui.adapters.NavigationDrawerAdapter;

/**
 * Created by Adam on 1/4/2015.
 */
public class Sidenav extends FrameLayout {
    private ListView categoryList;
    private ChangeListener listener = null;

    public static interface  ChangeListener {
        public void categoryClicked(Category c);
    }

    public Sidenav(Context context) {
        super(context);
        init();

    }

    public Sidenav(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_sidenav,this);
        categoryList = (ListView)findViewById(R.id.category_list);
        categoryList.setAdapter(new NavigationDrawerAdapter());
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = adapterView.getAdapter().getItem(i);
                if (o instanceof Category) {
                    Category c = (Category) o;
                    categoryClicked(c);
                } else if (o instanceof AndroidCategoryManager.CategoryDef) {
                    AndroidCategoryManager.CategoryDef c = (AndroidCategoryManager.CategoryDef) o;
                    categoryClicked(c.toCategory());
                }
            }
        });
    }

    private void categoryClicked(Category c) {
        if (listener != null) {
            listener.categoryClicked(c);
        }
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void blogCategoriesReceived(BlogCategoriesReceivedMessage message) {
        categoryList.setAdapter(new NavigationDrawerAdapter());

    }
}
