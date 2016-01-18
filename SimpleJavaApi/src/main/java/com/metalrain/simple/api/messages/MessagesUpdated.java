package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.responses.SimpleResponse;

/**
 * Confusing name, but it means that the Messages have been updated, and it's a message that's sent
 * over the bus
 *
 * Created by adam on 2014-10-16.
 */
public class MessagesUpdated extends ApiRootMessage {
    public MessagesUpdated(SimpleResponse response) { super(response); }

    public MessagesUpdated(Exception e) { super(e); }

    public MessagesUpdated() {}
}
