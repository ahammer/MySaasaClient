package com.metalrain.simple.api;

import com.metalrain.simple.api.model.*;
import com.metalrain.simple.api.responses.BlogApiService_getBlogCommentsResponse;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adam on 2014-10-16.
 */
public class ServiceState implements Serializable {
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
