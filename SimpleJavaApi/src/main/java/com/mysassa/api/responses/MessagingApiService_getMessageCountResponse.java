package com.mysassa.api.responses;

import com.google.gson.JsonObject;

/**
 * Created by adam on 2014-10-16.
 */
public class MessagingApiService_getMessageCountResponse extends SimpleResponse {
    public Long count = null;
    public MessagingApiService_getMessageCountResponse(JsonObject rootObject) {
        super(rootObject);
        if (success) {
            count = rootObject.get("data").getAsLong();
        } else {
            count = null;
        }
    }
}
