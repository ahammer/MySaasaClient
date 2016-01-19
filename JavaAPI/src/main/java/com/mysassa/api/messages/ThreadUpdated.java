package com.mysassa.api.messages;

import com.mysassa.api.model.Message;
import com.mysassa.api.responses.MessagingApiService_getMessagesResponse;

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
