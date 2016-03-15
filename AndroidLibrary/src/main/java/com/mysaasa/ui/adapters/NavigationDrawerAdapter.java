package com.mysaasa.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ApplicationSectionsManager;
import com.mysassa.api.model.Category;
import com.mysaasa.ui.views.BlogCategoryView;

import java.util.Collections;
import java.util.List;

/**
* Created by Adam on 1/4/2015.
*/
public class NavigationDrawerAdapter extends BaseAdapter {
    final List<ApplicationSectionsManager.CategoryDef> defs;



    public NavigationDrawerAdapter() {
        if (MySaasaApplication.getInstance()!= null) {
            defs = MySaasaApplication.getInstance().getAndroidCategoryManager().getItems();
            //cats = MySaasaApplication.getService().getBlogCategories();
        } else {
            defs = Collections.EMPTY_LIST;
            //cats = Collections.EMPTY_LIST;
        }
    }

    @Override
    public int getCount() {

        return defs.size();
    }

    @Override
    public Object getItem(int position) {
        return defs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlogCategoryView bcv = new BlogCategoryView(parent.getContext());
        if (defs.size()>0) {
            bcv.setCategory(((ApplicationSectionsManager.CategoryDef) getItem(position)).toCategory());
        } else {
            bcv.setCategory((Category) getItem(position));
        }
        return bcv;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
