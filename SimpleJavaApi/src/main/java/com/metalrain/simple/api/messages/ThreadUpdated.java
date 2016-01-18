package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.model.Message;
import com.metalrain.simple.api.responses.MessagingApiService_getMessagesResponse;

/**
 * Created by Adam on 4/6/2015.
 */
public class ThreadUpdated {
    public final Message rootMessage;
    public final MessagingApiService_getMessagesResponse result;

    public ThreadUpdated(MessagingApiService_getMessagesResponse result, Message rootMessage) {
        this.result = result;
        this.rootMessage = rootMessage;
    }
}
