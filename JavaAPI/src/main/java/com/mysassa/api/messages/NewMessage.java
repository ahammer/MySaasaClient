package com.mysassa.api.messages;

import com.mysassa.api.model.Message;

/**
 * Created by adamhammer2 on 2016-04-14.
 */
public class NewMessage {
    private final Message message;

    public NewMessage(Message m) {
        this.message = m;
    }
}
