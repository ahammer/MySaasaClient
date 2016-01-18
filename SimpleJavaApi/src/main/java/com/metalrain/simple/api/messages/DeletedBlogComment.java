package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.model.BlogComment;

/**
 * Created by Adam on 1/11/2015.
 */
public class DeletedBlogComment {
    public final BlogComment reference;

    public DeletedBlogComment(BlogComment reference) {
        this.reference = reference;
    }

    public BlogComment getReference() {
        return reference;
    }
}
