package com.mysassa.api.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysassa.api.model.BlogComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 1/5/2015.
 */
public class BlogApiService_getBlogCommentsResponse extends SimpleResponse {
    public List<BlogComment> comments = new ArrayList();
}
