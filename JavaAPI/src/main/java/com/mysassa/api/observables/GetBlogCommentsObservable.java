package com.mysassa.api.observables;

import com.mysassa.api.MySaasaGateway;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.responses.GetBlogCommentsResponse;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Adam on 3/6/2016.
 */
public class GetBlogCommentsObservable implements Observable.OnSubscribe<BlogComment> {
    private final MySaasaGateway gateway;
    private final BlogPost post;

    public GetBlogCommentsObservable(BlogPost post, MySaasaGateway gateway) {
        this.gateway = gateway;
        this.post = post;
    }

    @Override
    public void call(Subscriber<? super BlogComment> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            Call<GetBlogCommentsResponse> response = gateway.getBlogComments(post.id, 100);
        }
    }
}
