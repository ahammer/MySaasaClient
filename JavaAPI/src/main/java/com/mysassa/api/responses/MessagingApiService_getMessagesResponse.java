package com.mysassa.api.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysassa.api.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 2014-10-16.
 */
public class MessagingApiService_getMessagesResponse extends SimpleResponse {
    public List<Message> messages = new ArrayList();
    public MessagingApiService_getMessagesResponse(JsonObject rootObject) {
        super(rootObject);
        if (isSuccess()) {
            JsonArray array = rootObject.getAsJsonArray("data");
            for (int i=0;i<array.size();i++) {
                messages.add(Message.from(array.get(i).getAsJsonObject()));
            }
        }
    }
}
