package com.mysassa.api;

import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.observables.GetBlogPostsObservable;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

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
        Observable<BlogPost> observable = Observable.create(new GetBlogPostsObservable(c, mySaasa));
        mBlogPostCache.put(c,observable);
        return observable.subscribeOn(Schedulers.io());
    }



}
