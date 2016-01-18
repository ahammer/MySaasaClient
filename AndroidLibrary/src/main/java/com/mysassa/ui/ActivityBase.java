package com.mysassa.ui;

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
import android.view.MenuItem;
import android.view.Window;

import com.mysassa.R;
import com.mysassa.AndroidCategoryManager;

import com.mysassa.SimpleApplication;
import com.mysassa.api.ErrorMessage;
import com.mysassa.api.Service;
import com.mysassa.api.messages.BlogCategoriesReceivedMessage;
import com.mysassa.api.messages.NetworkStateChange;
import com.mysassa.api.messages.NewMessage;
import com.mysassa.api.model.Category;
import com.mysassa.ui.sidenav.Sidenav;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.functions.Action1;

/**
 * This is the base activity, it just will do some basic stuff like grab a reference to the service, etc. Bind callbacks for over-riding
 * <p/>
 * Created by administrator on 2014-06-30.
 */
public abstract class ActivityBase extends Activity  {
    private static volatile int FOREGROUND_REF_COUNT = 0;
    public static boolean isInForeground() {
        return FOREGROUND_REF_COUNT > 0;
    }

    public static final int REQUEST_CODE_SIGNIN = 10001;
    public Category selectedCategory;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;

    protected Sidenav sidenav;
    private BroadcastReceiver receiver;
    private Action1<Object> messageHook;

    protected boolean isSidenavOpen() {
        return mDrawerLayout.isDrawerOpen(sidenav);
    }
    public Category getSelectedCategory() {
        return selectedCategory;
    }
    public Service getService() {
        return SimpleApplication.getService();
    }
    public final boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        if (savedInstanceState == null) {
            selectedCategory  = new  Category(getString(R.string.defaultSection));
        }
        SimpleApplication.getService().bus.toObserverable().subscribe(messageHook = new Action1<Object>() {
            @Override
            public void call(Object o) {
                handleMessage(o);
            }
        });
    }

    //Handle incoming messages from the API
    protected void handleMessage(Object o) {
        if (o instanceof  NetworkStateChange) {
            networkUpdate((NetworkStateChange)o);
        } else if (o instanceof ErrorMessage) {
            ioError((ErrorMessage) o);

        } else if (o instanceof BlogCategoriesReceivedMessage) {
            categoriesUpdated((BlogCategoriesReceivedMessage) o);
        } else if (o instanceof NewMessage) {

            Crouton.makeText(this, "You have a new message", Style.INFO).show();
        }
    }


    public void networkUpdate(final NetworkStateChange event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (event.state) {
                    case Working: setProgressBarIndeterminateVisibility(true); break;
                    case Error:
                        Crouton.makeText(ActivityBase.this, "Network Error", Style.ALERT).show();
                        break;
                    default:
                        setProgressBarIndeterminateVisibility(false);
                }
            }
        });
    }

    public void ioError(final ErrorMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Crouton.makeText(ActivityBase.this, "Api Communication Error: "+message.e.getMessage(), Style.ALERT).show();

            }
        });
    }

    public void categoriesUpdated(final BlogCategoriesReceivedMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sidenav.blogCategoriesReceived(message);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkNetworkState();
            }
        },filter);
        FOREGROUND_REF_COUNT++;

    }

    private void checkNetworkState() {
        if (!isConnected()) {
            Crouton.makeText(this, "Network Disconnected", Style.ALERT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        FOREGROUND_REF_COUNT--;
    }

    protected void initializeSideNav() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sidenav = (Sidenav)findViewById(R.id.left_drawer);
        if (sidenav != null) sidenav.setListener(new Sidenav.ChangeListener() {
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
                R.drawable.menu_sidenav_launcher,  /* nav drawer icon to replace 'Up' caret */
                R.string.hello_world,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */

        );
        if (mDrawerLayout !=null) {
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerLayout.setFocusableInTouchMode(false);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

        }
    }


    protected void categoryChanged(Category c) {
        AndroidCategoryManager.CategoryDef def = SimpleApplication.getInstance().getAndroidCategoryManager().getCategoryDef(c);
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
                Crouton.makeText(ActivityBase.this, "Login Successful", Style.INFO).show();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

}
