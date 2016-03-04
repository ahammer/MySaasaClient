package com.mysassa.api.observables;

import com.mysassa.api.GetBlogCategoriesResponse;
import com.mysassa.api.IMySaasaGateway;
import com.mysassa.api.model.Category;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Get's the Categories from the server.
 */
public class CategoryObservable implements Observable.OnSubscribe<Category> {
    private final IMySaasaGateway gateway;

    public CategoryObservable(IMySaasaGateway gateway) {
        checkNotNull(gateway);
        this.gateway = gateway;
    }

    @Override
    public void call(Subscriber<? super Category> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            Call<GetBlogCategoriesResponse> blogCategoriesRequest = gateway.getBlogCategories();
            try {
                Response<GetBlogCategoriesResponse> response = blogCategoriesRequest.execute();
                for (Category c:response.body().getData()) {
                  subscriber.onNext(c);
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        }
    }
}
