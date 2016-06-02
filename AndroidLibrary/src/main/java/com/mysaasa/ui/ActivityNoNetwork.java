package com.mysaasa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import com.mysassa.R;

/**
 * Created by Adam on 6/1/2016.
 */
public class ActivityNoNetwork extends Activity {
    public static void start(Context ctx) {
        Intent intent = new Intent(ctx, ActivityNoNetwork.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
    }

}
