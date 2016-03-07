package com.mysassa.api.responses;

import com.mysassa.api.model.User;
import com.mysassa.api.responses.SimpleResponse;

/**
 * Created by Adam on 2/29/2016.
 */
public class LoginUserResponse extends SimpleResponse{
    private User data;

    public User getData() {
        return data;
    }
}
