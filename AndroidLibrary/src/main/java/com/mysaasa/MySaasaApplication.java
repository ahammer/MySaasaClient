package com.mysaasa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.common.eventbus.EventBus;
import com.mysassa.R;
import com.mysassa.api.MySaasaClient;
import com.mysassa.api.observables.PushIdGenerator;
import com.splunk.mint.Mint;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by administrator on 2014-06-30.
 */
public class MySaasaApplication extends Application {
    private static MySaasaApplication instance;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MySaasa";
    private static final String PROPERTY_REG_ID = "ID";
    private static final String PROPERTY_APP_VERSION = "APP_VERSION";
    private final EventBus bus = new EventBus();
    private MySaasaClient mySaasaClient;
    private ApplicationSectionsManager mSectionManager;
    private GoogleCloudMessaging gcm;
    private String regid;

    public static MySaasaApplication getInstance() {
        return instance;
    }

    public static MySaasaClient getService() {
        if (instance == null) throw new RuntimeException("Application not initialized");
        return instance.mySaasaClient;
    }

    @Override
    public void onCreate() {
        Mint.initAndStartSession(this, "a8d16bad");
        super.onCreate();


        instance = this;
        mSectionManager = new ApplicationSectionsManager(this);
        mySaasaClient = new MySaasaClient(getString(R.string.domain), getResources().getInteger(R.integer.port), getString(R.string.scheme));
        mySaasaClient.getAuthenticationManager().setPushIdGenerator(() -> {
            InstanceID instanceID = InstanceID.getInstance(MySaasaApplication.this);
            try {
                String gcmKey = getString(R.string.gcm_key);
                String token = instanceID.getToken(gcmKey, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                return token;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        });
        autoLogin();
    }

    public MySaasaClient getMySaasaClient() {
        return mySaasaClient;
    }

    private void autoLogin() {
        SharedPreferences sp = getSharedPreferences("autologin", 0);
        if (sp.contains("identifier") && sp.contains("password"))
            mySaasaClient.getAuthenticationManager().login(sp.getString("identifier", null), sp.getString("password", null))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {

                            },
                            error -> {
                                Toast.makeText(MySaasaApplication.this, "Error with auto-login: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                sp.edit().remove("password").remove("identifier").commit();
                            });

    }

    public ApplicationSectionsManager getAndroidCategoryManager() {
        return mSectionManager;
    }

    public void saveCredentials(String username, String password) {
        SharedPreferences sp = getSharedPreferences("autologin", 0);
        sp.edit().putString("identifier", username).commit();
        sp.edit().putString("password", password).commit();
    }

    public void clearCredentials() {
        SharedPreferences sp = getSharedPreferences("autologin", 0);
        sp.edit().remove("identifier").commit();
        sp.edit().remove("password").commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM mySaasaClient.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = 1;
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MySaasaApplication.class.getSimpleName() + "GCM",
                Context.MODE_PRIVATE);
    }

}
