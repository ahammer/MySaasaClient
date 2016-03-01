package com.adamhammer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Adam on 2/27/2016.
 */
public class ZoomingSpaceImageView extends ImageView {
    Stars stars = new Stars();

    public ZoomingSpaceImageView(Context context) {
        super(context);
    }

    public ZoomingSpaceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Stars.Point3D> points = stars.getStars();
    }

    public Matrix getInitialMatrix() {
        Matrix m = new Matrix();
        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        float scaleW = (float) viewWidth / drawableWidth;
        float scaleH = (float) viewHeight / drawableHeight;
        float scale = scaleH>scaleW?scaleH:scaleW;


        m.preScale(scale,scale);

        return m;
    }



    public void setCurrentTransition(float currentTransition) {
        setScaleType(ScaleType.MATRIX);
        Matrix m = getInitialMatrix();
        currentTransition = (float) Math.exp(currentTransition*3);
        //m.postRotate(currentTransition, getWidth()/2, getHeight()/2);
        m.postScale(1+currentTransition*0.12f, 1+currentTransition*0.12f, getWidth()/2,getHeight()/2);
        setImageMatrix(m);
    }



}
