package com.mysassa.api.messages;

import com.mysassa.api.responses.GetBlogPostsResponse;

/**
 * Created by adam on 2014-10-16.
 */
public class BlogPostsModified extends ApiRootMessage {

    public final GetBlogPostsResponse blogResponse;

    public BlogPostsModified(Exception e) {
        super(e);
        blogResponse = null;
    }

    public BlogPostsModified() {
        super();
        blogResponse = null;
    }

    public BlogPostsModified(GetBlogPostsResponse response) {
        this.blogResponse = response;
    }
}
