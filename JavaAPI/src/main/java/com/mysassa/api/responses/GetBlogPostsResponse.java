package com.mysassa.api.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysassa.api.model.BlogPost;

import java.util.ArrayList;

/**
 * Created by Adam on 12/28/2014.
 */
public class GetBlogPostsResponse extends SimpleResponse {
    public ArrayList<BlogPost> results = new ArrayList<BlogPost>();
}
