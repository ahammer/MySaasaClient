package com.mysassa.api.messages;

import com.mysassa.api.responses.SimpleResponse;

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
