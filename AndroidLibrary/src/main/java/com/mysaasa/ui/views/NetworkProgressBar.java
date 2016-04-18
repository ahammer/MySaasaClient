package com.mysaasa.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.MySaasaApplication;
import com.mysaasa.api.messages.NetworkStateChange;

/**
 * Created by Adam on 3/9/2016.
 */
public class NetworkProgressBar extends ProgressBar {
    public NetworkProgressBar(Context context) {
        super(context);
    }

    public NetworkProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            MySaasaApplication.getService().bus.register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleVisibility();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            MySaasaApplication.getService().bus.unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onMessageReceived(final NetworkStateChange msg) {
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            activity.runOnUiThread(this::handleVisibility);
        }

    }

    private void handleVisibility() {

        if (isInEditMode()) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(MySaasaApplication.getService().isNetworkBusy()?VISIBLE:GONE);
        }

    }
}
