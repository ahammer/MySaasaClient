package com.mysaasa.ui.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mysaasa.ui.views.EmptyListItem;

/**
 * Created by Adam on 1/5/2015.
 */
public abstract class EmptyListAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return "Empty";
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EmptyListItem item = new EmptyListItem(viewGroup.getContext());
        item.setText(getText());
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked();
            }
        });
        return item;

    }

    protected abstract void Clicked();

    protected abstract String getText();
}
