package com.metalrain.simple.api;

import com.metalrain.simple.api.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 4/6/2015.
 */
public class MessageManager {
    List<Message> rootMessages = new ArrayList<Message>();
    Map<Long, List<Message>> threads = new HashMap();

    public void refreshThread(List<Message> messages) {
        long id = messages.get(0).messageThreadRoot!=null?messages.get(0).messageThreadRoot.id:messages.get(0).id;
        List<Message> thread;
        thread = new ArrayList<Message>();
        threads.put(id, thread);
        for (Message m:messages) {
            thread.add(m);
        }
    }

    public void addRootMessages(List<Message> messages) {
        rootMessages.clear();
        for (Message m:messages) {
            if (!rootMessages.contains(m)) {
                rootMessages.add(m);

            }
        }
    }

    public List<Message> getRootMessages() {
        return rootMessages;
    }

    public List<Message> getThread(Message rootMessage) {
        if (threads.containsKey(rootMessage.id)) {
            return threads.get(rootMessage.id);
        }
        return Collections.EMPTY_LIST;

    }
}
