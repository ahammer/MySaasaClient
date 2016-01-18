package com.metalrain.simple.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metalrain.simple.AndroidCategoryManager;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.model.Category;
import com.metalrain.simple.ui.views.BlogCategoryView;

import java.util.Collections;
import java.util.List;

/**
* Created by Adam on 1/4/2015.
*/
public class NavigationDrawerAdapter extends BaseAdapter {
    final List<AndroidCategoryManager.CategoryDef> defs;
    //final List<Category> cats;


    public NavigationDrawerAdapter() {
        if (SimpleApplication.getInstance()!= null) {
            defs = SimpleApplication.getInstance().getAndroidCategoryManager().getItems();
            //cats = SimpleApplication.getService().getBlogCategories();
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
            bcv.setCategory(((AndroidCategoryManager.CategoryDef) getItem(position)).toCategory());
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
