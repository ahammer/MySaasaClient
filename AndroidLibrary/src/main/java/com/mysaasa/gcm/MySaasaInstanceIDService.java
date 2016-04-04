package com.mysaasa.gcm;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Adam on 4/3/2016.
 */
public class MySaasaInstanceIDService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        refreshAllTokens();
    }

    private void refreshAllTokens() {
        // assuming you have defined TokenList as
        // some generalized store for your tokens
        /*
        ArrayList<TokenList> tokenList = TokensList.get();
        InstanceID iid = InstanceID.getInstance(this);
        for(tokenItem : tokenList) {
            tokenItem.token =
                    iid.getToken(tokenItem.authorizedEntity,tokenItem.scope,tokenItem.options);
            // send this tokenItem.token to your server
        }
        */
        //TODO implement this as per google guidelines
        //https://developers.google.com/instance-id/guides/android-implementation#generate_a_token
        //It's when server request token refresh
    }
}
