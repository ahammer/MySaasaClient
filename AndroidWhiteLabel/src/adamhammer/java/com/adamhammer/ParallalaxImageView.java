package com.adamhammer;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Adam on 5/31/2016.
 */
public class ParallalaxImageView extends ImageView {
    public ParallalaxImageView(Context context) {
        super(context);
    }

    public ParallalaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Matrix getInitialMatrix() {
        Matrix m = new Matrix();
        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();
        int viewWidth = getWidth()*2;
        int viewHeight = getHeight()*2;
        float scaleW = (float) viewWidth / drawableWidth;
        float scaleH = (float) viewHeight / drawableHeight;
        float scale = scaleH>scaleW?scaleH:scaleW;
        m.preScale(scale,scale);
        return m;
    }

    public void setCurrentTransition(float currentTransition) {
        setScaleType(ScaleType.MATRIX);
        Matrix m = getInitialMatrix();
        m.postTranslate(-currentTransition*getWidth()/10, -currentTransition*getHeight());

        setImageMatrix(m);
    }
}
