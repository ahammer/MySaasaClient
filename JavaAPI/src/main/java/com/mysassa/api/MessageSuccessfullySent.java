package com.mysassa.api;

import com.mysassa.api.messages.ApiRootMessage;
import com.mysassa.api.responses.SimpleResponse;

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
