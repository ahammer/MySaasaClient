package com.mysassa.api.messages;

import com.mysassa.api.model.Message;

/**
 * Created by adamhammer2 on 2016-04-14.
 */
public class NewMessageInMemoryEvent {
    private final Message message;

    public NewMessageInMemoryEvent(Message m) {
        this.message = m;
    }
}
