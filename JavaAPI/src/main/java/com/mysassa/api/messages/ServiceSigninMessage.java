package com.mysassa.api.messages;

import com.mysassa.api.responses.SimpleResponse;

/**
 * This is
 * Created by adam on 2014-10-16.
 */
public class ServiceSigninMessage extends ApiRootMessage {
    public ServiceSigninMessage(SimpleResponse response) { super(response); }

    public ServiceSigninMessage(Exception e) { super(e); }

    public ServiceSigninMessage() {}
}
