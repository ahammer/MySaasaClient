package com.mysassa.api;

import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.observables.GetBlogCommentsObservable;
import com.mysassa.api.observables.GetBlogPostsObservable;
import com.mysassa.api.responses.PostCommentResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 3/3/2016.
 */
public class BlogManager {
    private final MySaasaClient mySaasa;
    private Map<Category, Observable<BlogPost>> mBlogPostCache = new HashMap<>();
    private Map<BlogPost, Observable<BlogComment>> mBlogCommentCache = new HashMap<>();

    public BlogManager(MySaasaClient mySaasaClient) {
        this.mySaasa = mySaasaClient;
    }


    /**
     * Returns a cached observable object for these blogposts
     * @param c
     * @return
     */
    public Observable<BlogPost> getBlogPostsObservable(final Category c) {
        //If in cache, return cache version
        if (mBlogPostCache.containsKey(c)) { return mBlogPostCache.get(c); }
        //Otherwise create one, put it in the cache and return it.
        Observable<BlogPost> observable = Observable.create(new GetBlogPostsObservable(c, mySaasa.gateway));
        mBlogPostCache.put(c,observable);
        return observable;
    }

    public Observable<BlogComment> getBlogCommentsObservable(BlogPost post) {
        //If in cache, return cache version
        //if (mBlogCommentCache.containsKey(post)) { return mBlogCommentCache.get(post); }
        //Otherwise create one, put it in the cache and return it.
        Observable<BlogComment> observable = Observable.create(new GetBlogCommentsObservable(post, mySaasa.gateway));
        //mBlogCommentCache.put(post,observable);
        return observable;
    }

    public Observable<PostCommentResponse> postBlogComment(final BlogPost post, final String text) {

        Observable<PostCommentResponse> observable = Observable.create(new Observable.OnSubscribe<PostCommentResponse>() {
            @Override
            public void call(Subscriber<? super PostCommentResponse> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<PostCommentResponse> call = mySaasa.gateway.postComment(post.id, text);
                    try {
                        Response<PostCommentResponse> response = call.execute();
                        System.out.println(response.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        observable.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe();
        return observable;

    }
}
