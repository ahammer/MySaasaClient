package com.mysaasa.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Adam on 1/10/2015.
 */
public class BlogDepthView extends View {
    private int depth;
    int[] colors = new int[]{
            Color.argb(192,64,64,192),
            Color.argb(192,64,128,128),
            Color.argb(192,64,192,64),
            Color.argb(192,128,128,64),
            Color.argb(192,192,64,64),
            Color.argb(192,128,64,128),
    };


    public BlogDepthView(Context context) {
        super(context);
    }

    public BlogDepthView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setDepth(int depth) {

        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = depth*10;


        this.depth = depth;
        String output = "";
        for (int i=0;i<depth;i++) output += " ";
        setBackgroundColor(colors[depth % colors.length]);

    }

    public void setColor(int c) {
        setBackgroundColor(c);
    }


}
