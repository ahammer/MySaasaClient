package com.metalrain.simple.api.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.metalrain.simple.api.model.BlogPost;

import java.util.ArrayList;

/**
 * Created by Adam on 12/28/2014.
 */
public class BlogApiService_getBlogPostsResponse extends SimpleResponse {
    public ArrayList<BlogPost> results = new ArrayList<BlogPost>();
    public BlogApiService_getBlogPostsResponse(JsonObject object) {
        super(object);
        if (isSuccess()) {
        if (object.get("data").isJsonArray()) {
            JsonArray blogPosts = object.getAsJsonArray("data");
            for (int i = 0; i < blogPosts.size(); i++) {
                results.add(new BlogPost(blogPosts.get(i).getAsJsonObject()));
            }
        } else if (object.get("data").isJsonObject()) {
                results.add(new BlogPost(object.get("data").getAsJsonObject()));
            }
        }
    }
}
