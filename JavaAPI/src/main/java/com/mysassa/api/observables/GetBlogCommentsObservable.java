package com.mysassa.api.observables;

import com.mysassa.api.MySaasaGateway;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.responses.GetBlogCommentsResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
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
            Call<GetBlogCommentsResponse> call = gateway.getBlogComments(post.id, 100);
            try {
                Response<GetBlogCommentsResponse> response = call.execute();
                for (BlogComment bc:response.body().getData())
                    subscriber.onNext(bc);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        }
    }
}
