package com.mysassa.api.messages;

import com.mysassa.api.responses.SimpleResponse;

/**
 * This is internal to the Service, first round broadcast is to the Service, so it can chain things together
 * When data is updated, external UX ones should be transmitted once preconditions are complete for UX switch
 *
 * Created by adam on 2014-10-16.
 */
public class MessageCountUpdated extends ApiRootMessage {
    public MessageCountUpdated(SimpleResponse response) { super(response); }

    public MessageCountUpdated(Exception e) { super(e); }

    public MessageCountUpdated() {}
}
