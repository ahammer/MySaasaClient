package com.mysaasa;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mysaasa.api.MessageManager;
import com.mysaasa.ui.ActivityMain;
import com.mysassa.R;
import com.mysaasa.api.MySaasaClient;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by administrator on 2014-06-30.
 */
public class MySaasaApplication extends Application {
    private static MySaasaApplication instance;
    private MySaasaClient mySaasaClient;
    private ApplicationSectionsManager mSectionManager;
    private MessageNotificationManager mMessageNotificationManager;
    public static MySaasaApplication getInstance() {
        return instance;
    }

    public static MySaasaClient getService() {
        if (instance == null) throw new RuntimeException("Application not initialized");
        return instance.mySaasaClient;
    }

    @Override
    public void onCreate() {
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
        mMessageNotificationManager = new MessageNotificationManager(this, mySaasaClient);
        mMessageNotificationManager.start();
        autoLogin();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mMessageNotificationManager.stop();
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

}
