package com.adamhammer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mysassa.whitelabel.R;

/**
 * Created by Adam on 2/28/2016.
 */
public class MyPhotosAdapter extends BaseAdapter {
    final static int[] image_ids = new int[]{R.drawable.photo1, R.drawable.photo2, R.drawable.photo3};


    @Override
    public int getCount() {
        return image_ids.length;
    }

    @Override
    public Object getItem(int i) {
        return image_ids[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ZoomingSpaceImageView iv;
        if (view != null && view instanceof ImageView) {
            iv = (ZoomingSpaceImageView) view;
        } else {
            iv = new ZoomingSpaceImageView(viewGroup.getContext());
        }
        iv.setImageResource(image_ids[i]);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) AdamsSplash.dpToPx(viewGroup.getContext(), 300));
        iv.setLayoutParams(lp);
        iv.setCurrentTransition(0);

        return iv;
    }
}
