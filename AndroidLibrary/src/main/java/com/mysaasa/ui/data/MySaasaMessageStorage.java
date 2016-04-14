package com.mysaasa.ui.data;

import com.mysassa.api.model.Message;
import com.mysassa.api.model.User;

import java.util.List;

/**
 * Created by Adam on 4/11/2016.
 */
public interface MySaasaMessageStorage {
    public List<Message> getRootMessage(User user);
    public void storeMessage(Message m);

    Message getMessageById(long id);
}
