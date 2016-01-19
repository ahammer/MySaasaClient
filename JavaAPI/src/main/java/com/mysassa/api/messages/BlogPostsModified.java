package com.mysassa.api.messages;

import com.mysassa.api.responses.BlogApiService_getBlogPostsResponse;

/**
 * Created by adam on 2014-10-16.
 */
public class BlogPostsModified extends ApiRootMessage {

    public final BlogApiService_getBlogPostsResponse blogResponse;

    public BlogPostsModified(Exception e) {
        super(e);
        blogResponse = null;
    }

    public BlogPostsModified() {
        super();
        blogResponse = null;
    }

    public BlogPostsModified(BlogApiService_getBlogPostsResponse response) {
        this.blogResponse = response;
    }
}
