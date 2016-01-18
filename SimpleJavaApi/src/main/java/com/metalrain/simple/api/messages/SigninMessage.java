package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.responses.SimpleResponse;

/**
 * Created by adam on 2014-10-16.
 */
public class SigninMessage extends ApiRootMessage {
    public SigninMessage(SimpleResponse response) { super(response); }

    public SigninMessage(Exception e) { super(e); }

    public SigninMessage() {}
}
