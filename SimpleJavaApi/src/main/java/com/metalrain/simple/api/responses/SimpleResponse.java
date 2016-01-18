package com.metalrain.simple.api.responses;

import com.google.gson.JsonObject;
import com.metalrain.simple.NotAuthorizedException;
import com.metalrain.simple.api.Gateway;

import java.io.Serializable;

/**
 * This class Proxies the Gatewayfunction to a Response Class
 *
 * By default it just handles with a default response.
 * Created by adam on 2014-10-16.
 */
public class SimpleResponse implements Serializable {
    /**
     * This is where we handle JsonObject for various functions, abstracted away from the generated code.
     * @param jsonObject
     * @param function
     * @return
     */
    public static SimpleResponse create(JsonObject jsonObject, Gateway.API_FUNCTIONS function) throws NotAuthorizedException {
        if (jsonObject.has("authorizationFailure") ) {
            throw new NotAuthorizedException();
        }
        switch (function) {
            case UserApiService_createUser:
            case UserApiService_loginUser:
                return new UserApiService_loginUserResponse(jsonObject);


            case CategoryApiService_getBlogCategories:
            case CategoryApiService_getProductCategories:
                return new CategoryService_getCategoriesResponse(jsonObject);

            case BlogApiService_getBlogPosts:
            case BlogApiService_getBlogPostById:
                return new BlogApiService_getBlogPostsResponse(jsonObject);

            case BlogApiService_getBlogComments:
                return new BlogApiService_getBlogCommentsResponse(jsonObject);

            case MessagingApiService_getMessageCount:
                return new MessagingApiService_getMessageCountResponse(jsonObject);

            case MessagingApiService_getMessages:
            case MessagingApiService_getThread:
                return new MessagingApiService_getMessagesResponse(jsonObject);

            default:
                return new SimpleResponse(jsonObject);
        }

    }


    public final boolean success;
    protected String data;

    public SimpleResponse(JsonObject object) {
        if (object.has("success")) {
            success = object.get("success").getAsBoolean();
            if (success == false && object.has("data")) {
                data = object.get("data").toString();
            }
        } else {
            success = false;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }


}
