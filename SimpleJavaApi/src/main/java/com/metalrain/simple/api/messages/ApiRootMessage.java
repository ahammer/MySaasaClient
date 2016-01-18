package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.responses.SimpleResponse;

/**
 * Some basic information common to all responses
 *
 * Created by adam on 2014-10-16.
 */
public class ApiRootMessage {
    public final Exception e;
    public final SimpleResponse response;

    public ApiRootMessage(SimpleResponse response) {
        this.response = response;
        this.e = null;
    }

    public ApiRootMessage(Exception e) {
        this.e = e;
        this.response = null;
    }
    public ApiRootMessage() {
        e = null;
        response = null;
    }

    public boolean isSuccess() {
        return (response != null && response.isSuccess());
    }
}
