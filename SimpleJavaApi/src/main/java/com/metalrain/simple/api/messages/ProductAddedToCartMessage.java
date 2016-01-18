package com.metalrain.simple.api.messages;

/**
 * Created by adam on 2014-10-16.
 */
public class ProductAddedToCartMessage extends ApiRootMessage {



    public ProductAddedToCartMessage(Exception e) {
        super(e);
    }

    public ProductAddedToCartMessage() {
        super();
    }
}
