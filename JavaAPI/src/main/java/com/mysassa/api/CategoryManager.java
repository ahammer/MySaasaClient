package com.mysassa.api;

import com.mysassa.api.model.Category;

import rx.Observable;

/**
 * Created by Adam on 3/3/2016.
 */
public class CategoryManager {
    private final MySaasaClient mySaasa;
    private Observable<Category> categoryObservable;

    public CategoryManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }
}
