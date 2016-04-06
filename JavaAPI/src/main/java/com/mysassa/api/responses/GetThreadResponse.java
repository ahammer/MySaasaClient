package com.mysassa.api.responses;

import com.mysassa.api.model.Message;

import java.util.List;

/**
 * Created by Adam on 2/29/2016.
 */
public class GetThreadResponse extends SimpleResponse {
    public List<Message> data;
}
