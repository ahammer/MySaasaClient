package com.mysassa.api.responses;

import com.google.gson.JsonObject;

/**
 * Created by Adam on 2/29/2016.
 */
public class SimpleResponse {
    protected boolean success;
    public String message;

    public SimpleResponse(){}


    public boolean isSuccess() {
        return this.success;
    }

}
