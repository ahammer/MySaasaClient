package com.mysaasa.ui.sidenav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mysassa.R;
import com.mysaasa.ApplicationSectionsManager;

import com.mysaasa.api.model.Category;
import com.mysaasa.ui.adapters.NavigationDrawerAdapter;

import java.util.List;

import rx.Subscription;

/**
 * Created by Adam on 1/4/2015.
 */
public class LeftNavigationFrameLayout extends FrameLayout {
    private ListView categoryList;
    private ChangeListener listener = null;

    private Subscription categorySubscription;
    private List<Category> categories;

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public static interface  ChangeListener {
        public void categoryClicked(Category c);
    }

    public LeftNavigationFrameLayout(Context context) {
        super(context);
        init();

    }

    public LeftNavigationFrameLayout(Context context, AttributeSet attrs) {
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
                } else if (o instanceof ApplicationSectionsManager.CategoryDef) {
                    ApplicationSectionsManager.CategoryDef c = (ApplicationSectionsManager.CategoryDef) o;
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


}
