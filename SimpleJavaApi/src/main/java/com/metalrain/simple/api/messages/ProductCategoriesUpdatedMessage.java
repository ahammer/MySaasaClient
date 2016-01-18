package com.metalrain.simple.api.messages;

import com.metalrain.simple.api.responses.CategoryService_getCategoriesResponse;
import com.metalrain.simple.api.responses.SimpleResponse;

/**
 * Created by adam on 2014-10-16.
 */
public class ProductCategoriesUpdatedMessage extends ApiRootMessage {

    public ProductCategoriesUpdatedMessage(Exception e) {
        super(e);
    }

    public ProductCategoriesUpdatedMessage() {
        super();
    }

    public ProductCategoriesUpdatedMessage(SimpleResponse result) {
     super(result);
    }
}
