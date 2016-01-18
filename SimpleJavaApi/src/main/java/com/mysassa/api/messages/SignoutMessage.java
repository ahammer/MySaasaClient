package com.mysassa.api.messages;

import com.mysassa.api.responses.SimpleResponse;

/**
 * Created by adam on 2014-10-16.
 */
public class SignoutMessage extends ApiRootMessage {
    public SignoutMessage(SimpleResponse response) { super(response); }

    public SignoutMessage(Exception e) { super(e); }

    public SignoutMessage() {}
}
