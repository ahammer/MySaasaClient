package com.mysassa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.common.eventbus.EventBus;
import com.mysassa.api.MySaasaClient;
import com.splunk.mint.Mint;

import java.io.IOException;

/**
 * Created by administrator on 2014-06-30.
 */
public class MySaasaAndroidApplication extends Application {
    private static MySaasaAndroidApplication instance;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MySaasa";
    private static final String PROPERTY_REG_ID = "ID";
    private static final String PROPERTY_APP_VERSION = "APP_VERSION";
    private final EventBus bus = new EventBus();
    private MySaasaClient mySaasaClient;
    private ApplicationSectionsManager mSectionManager;
    private GoogleCloudMessaging gcm;
    private String regid;

    public static MySaasaAndroidApplication getInstance() {
        return instance;
    }
    public static MySaasaClient getService() {
        return instance.mySaasaClient;
    }

    @Override
    public void onCreate() {
        Mint.initAndStartSession(this, "a8d16bad");
        super.onCreate();
        instance = this;
        mSectionManager = new ApplicationSectionsManager(this);
        mySaasaClient = new MySaasaClient(getString(R.string.domain), getResources().getInteger(R.integer.port), getString(R.string.scheme));
        registerGoogleCloudMessaging();
    }

    private void registerGoogleCloudMessaging() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
    }

    private void autoLogin() {
        SharedPreferences sp = getSharedPreferences("autologin",0);
        if (sp.contains("identifier") && sp.contains("password"))
            mySaasaClient.getLoginManager().login(sp.getString("identifier", null), sp.getString("password", null));
    }

    public ApplicationSectionsManager getAndroidCategoryManager() {
        return mSectionManager;
    }

    public void saveCredentials(String username, String password) {
        SharedPreferences sp = getSharedPreferences("autologin",0);
        sp.edit().putString("identifier", username).commit();
        sp.edit().putString("password", password).commit();


    }
    public void clearCredentials() {
        SharedPreferences sp = getSharedPreferences("autologin",0);
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
     *         registration ID.
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
        return getSharedPreferences(MySaasaAndroidApplication.class.getSimpleName()+"GCM",
                Context.MODE_PRIVATE);
    }

    private void registerInBackground() {
        try {
            new AsyncTask() {

                @Override
                protected Object doInBackground(Object[] objects) {
                    String msg = "";
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(MySaasaAndroidApplication.this);
                        }
                        String key = getString(R.string.gcm_key);
                        regid = gcm.register(key);
                        msg = "Device registered, registration ID=" + regid;
                        Log.i(TAG, msg);

                        // You should send the registration ID to your server over HTTP,
                        // so it can use GCM/HTTP or CCS to send messages to your app.
                        // The request to your server should be authenticated if your app
                        // is using accounts.
                        //mySaasaClient.registerGcmSenderId(regid);
                        //sendRegistrationIdToBackend();

                        // For this demo: we don't need to send it because the device
                        // will send upstream messages to a server that echo back the
                        // message using the 'from' address in the message.

                        // Persist the registration ID - no need to register again.
                        storeRegistrationId(MySaasaAndroidApplication.this, regid);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        // If there is an error, don't just keep trying to register.
                        // Require the user to click a button again, or perform
                        // exponential back-off.
                    }
                    return msg;

                }
            }.execute(null, null, null);
        } catch (SecurityException e) {
            Toast.makeText(this, "Can not register on GCM", Toast.LENGTH_SHORT).show();
        }

    }

    private void storeRegistrationId(MySaasaAndroidApplication simpleApplication, String regid) {

    }


    public EventBus getBus() {
        return bus;
    }
}
