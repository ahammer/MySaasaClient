package com.mysassa.api.responses;

import com.google.gson.JsonObject;
import com.mysassa.api.model.User;

/**
 * Created by adam on 2014-10-16.
 */
public class UserApiService_loginUserResponse extends SimpleResponse {
    public final User user;
    public UserApiService_loginUserResponse(JsonObject rootObject) {
        super(rootObject);
        if (success) {
            user = new User(rootObject.getAsJsonObject("data"));
        } else {
            user = null;
        }
    }
}
