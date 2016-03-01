package com.mysassa.api.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysassa.api.model.BlogPost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 12/28/2014.
 */
public class GetBlogPostsResponse extends SimpleResponse {
        private ArrayList<BlogPost> data = new ArrayList<BlogPost>();
        public List<BlogPost> getData() {
                return Collections.unmodifiableList(data);
        }
}
