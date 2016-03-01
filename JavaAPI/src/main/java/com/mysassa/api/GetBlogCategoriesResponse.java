package com.mysassa.api;

import com.mysassa.api.model.Category;

import java.util.List;

/**
 * Created by Adam on 2/29/2016.
 */
public class GetBlogCategoriesResponse {
    List<Category> results;

    public List<Category> getResults() {
        return results;
    }
}
