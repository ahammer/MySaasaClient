package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.responses.SimpleResponse;

/**
 * Created by adam on 2014-10-16.
 */
public class SignoutMessage extends ApiRootMessage {
    public SignoutMessage(SimpleResponse response) { super(response); }

    public SignoutMessage(Exception e) { super(e); }

    public SignoutMessage() {}
}
