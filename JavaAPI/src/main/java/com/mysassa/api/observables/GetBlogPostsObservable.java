package com.mysassa.api.observables;

import com.mysassa.api.MySaasaGateway;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.responses.GetBlogPostsResponse;

import java.io.IOException;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/3/2016.
 */
public class GetBlogPostsObservable implements Observable.OnSubscribe<BlogPost> {
    private final Category category;
    private final MySaasaGateway gateway;
    private GetBlogPostsResponse response;

    public GetBlogPostsObservable(Category category, MySaasaGateway gateway) {
        this.category = category;
        this.gateway = gateway;
    }

    @Override
    public void call(Subscriber<? super BlogPost> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            if (response != null) {

            }

            try {
                if (response == null) {
                    Call<GetBlogPostsResponse> call = gateway.getBlogPosts(category.name, 0, 100, "priority", "DESC");
                    this.response = call.execute().body();
                }

                if (!this.response.isSuccess()) {
                    subscriber.onError(new RuntimeException(response.getMessage()));
                }
                for (BlogPost bp : response.getData()) {
                    subscriber.onNext(bp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            subscriber.onCompleted();
        }
    }
}
