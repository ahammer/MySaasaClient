package com.adamhammer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 3/31/2016.
 */
public class JoiningAdapter extends BaseAdapter {
    final ListAdapter mAdapter1;
    final ListAdapter mAdapter2;
    List<Entry> entries = new ArrayList<Entry>();

    class Entry {
        final boolean inAdapter1;
        final int pos;

        Entry(boolean inAdapter1, int pos) {
            this.inAdapter1 = inAdapter1;
            this.pos = pos;
        }
    }

    public JoiningAdapter(ListAdapter a1, ListAdapter a2) {
        this.mAdapter1 = a1;
        this.mAdapter2 = a2;
        init();
    }

    private void init() {
        float ratio = mAdapter1.getCount() / (float) mAdapter2.getCount();

        float count = 0;
        int pos1 = 0;
        int pos2 = 0;
        while (pos1 < mAdapter1.getCount() || pos2 < mAdapter2.getCount()) {
            if (Math.floor(count + ratio) == Math.floor(count)) {
                if (pos2 >= mAdapter2.getCount()) return;
                entries.add(new Entry(false, pos2++));
            } else {
                if (pos1 >= mAdapter1.getCount()) return;
                entries.add(new Entry(true, pos1++));

            }
            count += ratio;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return entries.get(position).inAdapter1 ? 0 : 1;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int i) {
        return ((entries.get(i).inAdapter1) ? mAdapter1 : mAdapter2).getItem(entries.get(i).pos);
    }

    @Override
    public long getItemId(int i) {
        return ((entries.get(i).inAdapter1) ? mAdapter1 : mAdapter2).getItemId(entries.get(i).pos);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return ((entries.get(i).inAdapter1) ? mAdapter1 : mAdapter2).getView(entries.get(i).pos, view, viewGroup);
    }
}
