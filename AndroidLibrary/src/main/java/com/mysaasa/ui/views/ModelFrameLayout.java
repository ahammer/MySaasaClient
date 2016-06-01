package com.mysaasa.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * This class is a Wrapper for FrameLayout to create model-backed views
 *
 *
 */
public abstract class ModelFrameLayout<T> extends FrameLayout{

    T model;

    public ModelFrameLayout(int layoutId, Context context) {
        super(context);
        inflate(layoutId);
    }

    public ModelFrameLayout(int layoutId, Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(layoutId);
    }

    public ModelFrameLayout(int layoutId, Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(layoutId);
    }

    void inflate(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutId,this);
        bindViews();
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        if (this.model == model) return;
        this.model = model;
        if (model == null) clearViews();
        else populateViews();
    }

    /**
     * When a null model is set, we need to clear our views or switch to a empty record state
     */
    protected abstract void clearViews();

    /**
     * After a model is set, we need to populate the views as appropriate for the model
     */
    protected abstract void populateViews();

    /**
     * After the layout is inflated, we need to bind our views, either with butterknife
     * or other.
     */
    protected abstract void bindViews();
}
