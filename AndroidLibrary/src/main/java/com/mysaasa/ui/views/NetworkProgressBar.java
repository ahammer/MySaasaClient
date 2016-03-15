package com.mysaasa.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.mysaasa.MySaasaAndroidApplication;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.messages.NetworkStateChange;

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
        MySaasaAndroidApplication.getService().bus.register(this);
        handleVisibility();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MySaasaAndroidApplication.getService().bus.unregister(this);
    }

    @Subscribe
    public void onMessageReceived(final NetworkStateChange msg) {
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),msg.isBusy()+" "+msg.getDepth() , Toast.LENGTH_SHORT).show();
                    handleVisibility();
                }
            });
        }

    }

    private void handleVisibility() {

        if (isInEditMode()) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(MySaasaAndroidApplication.getService().isNetworkBusy()?VISIBLE:GONE);
        }

    }
}
