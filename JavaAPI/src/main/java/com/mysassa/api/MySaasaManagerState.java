package com.mysassa.api;

import com.mysassa.api.model.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by adam on 2014-10-16.
 */
public class MySaasaManagerState implements Serializable {
    public MessageManager messages = new MessageManager();
    public List<Category> blogCategories = Collections.EMPTY_LIST;

    String current_username = "", current_password = "";
    public User user;

    public Long messageCount = null;
    public String gcm_sender_id = null;
    public boolean authenticated = false;
    public boolean gcm_sent = false;
    public List<Category> product_categories;

    public boolean isAuthenticated() {
        return authenticated;
    }
}
