package com.mysassa.api.responses;

import com.mysassa.api.model.BlogComment;

import java.util.List;

/**
 * Created by Adam on 2/29/2016.
 */
public class GetBlogCommentsResponse {
    List<BlogComment> data;

    public List<BlogComment> getData() {
        return data;
    }
}
