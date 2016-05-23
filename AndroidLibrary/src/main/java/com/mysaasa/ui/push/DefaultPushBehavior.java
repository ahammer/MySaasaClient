package com.mysaasa.ui.push;

import android.app.Activity;

import com.mysaasa.MySaasaApplication;

/**
 * Created by Adam on 5/22/2016.
 */
public abstract class DefaultPushBehavior implements PushBehavior {
    final Activity activity;

    protected DefaultPushBehavior(Activity activity) {
        this.activity = activity;
    }


    @Override
    public final void start() {
        MySaasaApplication.getService().bus.register(this);
    }

    @Override
    public final void stop() {
        MySaasaApplication.getService().bus.unregister(this);
    }
}
