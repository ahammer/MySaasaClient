package com.mysassa.api;

import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.observables.GetBlogPostsObservable;
import com.mysassa.api.observables.StandardMySaasaObservable;
import com.mysassa.api.responses.PostToBlogResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
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
        Observable<BlogPost> observable = Observable.create(new GetBlogPostsObservable(c, mySaasa));

        return observable.subscribeOn(Schedulers.io()).onBackpressureBuffer();
    }


    public Observable<PostToBlogResponse> postToBlog(final String title, final String subtitle, final String summary, final String body, final String category) {
        return Observable.create(new PostToBlogObservableBase(mySaasa, title, subtitle, summary, body, category))
                .subscribeOn(Schedulers.io());
    }

    private static class PostToBlogObservableBase extends StandardMySaasaObservable<PostToBlogResponse> {
        private final String title;
        private final String subtitle;
        private final String summary;
        private final String body;
        private final String category;

        public PostToBlogObservableBase(MySaasaClient client, String title, String subtitle, String summary, String body, String category) {
            super(client);
            this.title = title;
            this.subtitle = subtitle;
            this.summary = summary;
            this.body = body;
            this.category = category;
        }

        @Override
        public boolean postResponse(PostToBlogResponse response) {
            return true;
        }

        @Override
        protected Call<PostToBlogResponse> getNetworkCall() {

            return getMySaasa().getGateway().postToBlog(title, subtitle, summary, body, category);
        }
    }
}
