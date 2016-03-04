package com.mysassa.api.observables;

import com.mysassa.api.IMySaasaGateway;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.responses.GetBlogPostsResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/3/2016.
 */
public class GetBlogPostsObservable implements Observable.OnSubscribe<BlogPost> {
    private final Category category;
    final IMySaasaGateway gateway;
    
    public GetBlogPostsObservable(Category category, IMySaasaGateway gateway) {
        this.category = category;
        this.gateway = gateway;
    }

    @Override
    public void call(Subscriber<? super BlogPost> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            Call<GetBlogPostsResponse> call = gateway.getBlogPosts(category.name, 0, 100, "priority", "DESC");
            try {
                Response<GetBlogPostsResponse> response = call.execute();
                if (!response.body().isSuccess()) {
                    subscriber.onError(new RuntimeException(response.message()));
                }
                for (BlogPost bp : response.body().getData()) {
                    subscriber.onNext(bp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            subscriber.onCompleted();
        }
    }
}
