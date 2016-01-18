package com.metalrain.simple.api;

import com.metalrain.simple.api.messages.ApiRootMessage;
import com.metalrain.simple.api.responses.SimpleResponse;

/**
 * Created by adam on 15-02-25.
 */
public class MessageSuccessfullySent extends ApiRootMessage{
    public final long requestCode;

    public MessageSuccessfullySent(SimpleResponse result, long requestCode) {
        super(result);
        this.requestCode = requestCode;

    }
}
