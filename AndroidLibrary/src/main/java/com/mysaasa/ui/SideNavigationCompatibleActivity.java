package com.mysaasa.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.sidenav.LeftNavigationFrameLayout;
import com.mysassa.R;
import com.mysaasa.ApplicationSectionsManager;

import com.mysassa.api.MySaasaClient;
import com.mysassa.api.model.Category;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * This is the base activity, it just will do some basic stuff like grab a reference to the service, etc. Bind ContactViewCallbacks for over-riding
 * <p/>
 * Created by administrator on 2014-06-30.
 */
public abstract class SideNavigationCompatibleActivity extends AppCompatActivity {
    private static volatile int FOREGROUND_REF_COUNT = 0;

    public static boolean isInForeground() {
        return FOREGROUND_REF_COUNT > 0;
    }

    public static final int REQUEST_CODE_SIGNIN = 10001;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected LeftNavigationFrameLayout sidenav;
    private BroadcastReceiver connectivityChangedReceiver;
    public Category selectedCategory;


    protected boolean isSidenavOpen() {
        return mDrawerLayout.isDrawerOpen(sidenav);
    }
    public Category getSelectedCategory() {
        return selectedCategory;
    }
    public MySaasaClient getService() {
        return MySaasaApplication.getService();
    }

    public final boolean isConnected() {
        NetworkInfo activeNetwork = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            selectedCategory  = new Category(getString(R.string.defaultSection));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkNetworkState();
            }
        },filter);
        FOREGROUND_REF_COUNT++;
        MySaasaApplication.getService().bus.register(this);
        setProgressBarIndeterminate(MySaasaApplication.getService().isNetworkBusy());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityChangedReceiver);
        FOREGROUND_REF_COUNT--;
        MySaasaApplication.getService().bus.unregister(this);
    }

    private void checkNetworkState() {
        if (!isConnected()) {
            Crouton.makeText(this, "Network Disconnected", Style.ALERT).show();
        }
    }

    protected void initializeSideNav() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sidenav = (LeftNavigationFrameLayout)findViewById(R.id.left_drawer);
        if (sidenav != null) sidenav.setListener(new LeftNavigationFrameLayout.ChangeListener() {
            @Override
            public void categoryClicked(Category c) {
                selectedCategory = c;
                categoryChanged(c);
                mDrawerLayout.closeDrawers();
            }
        });

        if (mDrawerLayout!=null) mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_reorder_black_24dp,  /* nav drawer icon to replace 'Up' caret */
                R.string.open_drawer,  /* "open drawer" description */
                R.string.close_drawer  /* "close drawer" description */

        );

        if (mDrawerLayout !=null) {
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerLayout.setFocusableInTouchMode(false);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }



    protected void categoryChanged(Category c) {
        ApplicationSectionsManager.CategoryDef def = MySaasaApplication.getInstance().getAndroidCategoryManager().getCategoryDef(c);
        ActivityMain.startFreshTop(this, c);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGNIN) {
            if (resultCode == Activity.RESULT_OK) {
                Crouton.makeText(SideNavigationCompatibleActivity.this, "Login Successful", Style.INFO).show();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

}
