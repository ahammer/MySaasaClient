package com.mysassa.api.messages;

import com.mysassa.api.model.BlogPost;

/**
 * Created by Adam on 1/15/2015.
 */
public class DeletedBlogPost {
    public final BlogPost post;

    public DeletedBlogPost(BlogPost post) {
        this.post = post;
    }
}