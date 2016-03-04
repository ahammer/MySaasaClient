package com.mysassa.api;

import com.google.common.eventbus.EventBus;
import com.mysassa.api.enums.NetworkState;
import com.mysassa.api.messages.NetworkStateChange;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.model.Message;
import com.mysassa.api.observables.CategoryObservable;
import com.mysassa.api.observables.GetBlogPostsObservable;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by administrator on 2014-06-29.
 */

/**
 * Some logic and state/threading/messaging can be built into this layer
 *
 * Once things are downloaded, they are stored in state.
 *
 * When authentication fails, it should automatically re-authenticate now
 *
 * Try Call -> Authentication Failure? Yes -> Try Login -> Success -> Retry Call
 *
 */
public class MySaasaClient {
    private final IMySaasaGateway gateway;
    private final MySaasaManagerState state = new MySaasaManagerState();

    //We use Guava event bus sometimes
    public final EventBus bus = new EventBus();

    //Observables
    private Observable<LoginUserResponse> mLoginResponseObservable;
    private Observable<Category>    categoryObservable;

    private Map<Category, Observable<BlogPost>>
            mBlogPostCache = new HashMap<>();


    public Observable<Category> getCategoryObservable() {
        if (categoryObservable == null) {
            categoryObservable = Observable.create(new CategoryObservable(gateway)).cache();
        }
        return categoryObservable;
    }


    /**
     * Construct the service on a domain/port.
     *
     * Point it to a Simple platform Domain, and the running port, and the rest should work.
     *
     * @param domain
     * @param port
     */
    public MySaasaClient(String domain, int port, String scheme) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        gateway = new Retrofit.Builder()
                .baseUrl(scheme+"://"+domain+":"+port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client )
                .build()
                .create(IMySaasaGateway.class);

    }


    /**
     * Get an observable for the Blog Posts in a particular category
     * @param c
     * @return
     */
    public Observable<BlogPost> getBlogPostsObservable(final Category c) {
        if (mBlogPostCache.containsKey(c)) {
            return mBlogPostCache.get(c);
        }

        Observable<BlogPost> observable = Observable.create(new GetBlogPostsObservable(c, gateway));
        mBlogPostCache.put(c,observable);
        return observable;
    }

    public MySaasaManagerState getState() {
        return state;
    }


    /**
     * Log the user in. Will not execute if already in progress,
     *
     * @param username
     * @param password
     */
    public Observable<LoginUserResponse> login(final String username, final String password) {
        if (mLoginResponseObservable != null) return mLoginResponseObservable;
        mLoginResponseObservable = Observable.create(new Observable.OnSubscribe<LoginUserResponse>() {
            @Override
            public void call(Subscriber<? super LoginUserResponse> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<LoginUserResponse> loginUserResponseCall = gateway.loginUser(username, password);
                    try {
                        Response<LoginUserResponse> response = loginUserResponseCall.execute();
                        subscriber.onNext(response.body());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }

            }
        }).cache();
        return mLoginResponseObservable;
    }

    public void registerGcmSenderId(String gcm_sender_id) {
        state.gcm_sender_id = gcm_sender_id;
        sendGcmSenderId(state.gcm_sender_id);
    }

    private void sendGcmSenderId(final String gcm_sender_id) {

    }


    public void signout() {

    }

    public void createUser(final String username, final String password) {

    }

    public void commentOnBlog(final long id, final String comment) {

    }

    public void postToBlog(final String title, final String subtitle, final String summary, final String value, final String category) {

    }

    public void updateBlogPost(final BlogPost post, final String title,final String subtitle, final String summary, final String value) {

    }

    public void updateBlogComment(final BlogComment comment, final String text) {

    }

    public void replyToComment(final long comment_id, final String comment) {

    }

    public void deleteBlogComment(final BlogComment comment) {
        checkNotNull(comment);

    }

    public void deleteBlogPost(final BlogPost post) {

    }

    /**
     * Helper to turn a media into a url string

    public String getMediaUrl(Media media) {
        String s = gateway.domain + "/media/" + media.uid;
        return s;
    }
     */


    public List<BlogPost> getBlogPosts(Category category, int page, int count, String order, String direction) {
        return Collections.EMPTY_LIST;
    }

    public List<BlogComment> getBlogComments(long id, int count, boolean force) {
        return Collections.EMPTY_LIST;

    }

    public void getBlogPostById(final long BlogPostId) {
       }

    void retrieveBlogComments(final long id, final int count) {


    }

    private void notifyNetworkWorking(NetworkState state) {
        bus.post(new NetworkStateChange(state));
    }


    public void replyToMessage(final Message rootMessage, final String s) {

    }

    public Observable<Integer> getMessageCount() {
        return Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<GetMessageCountResponse> request = gateway.getMessageCount();
                    try {
                        Response<GetMessageCountResponse> response = request.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //subscriber.onNext(re);

                }
            }
        });
    }

    public Observable requestMessageThread(Message rootMessage) {
        throw new RuntimeException("not implemented");
    }

    public long sendMessage(String admin, String s, String s1, String s2, String s3, String s4) {
        return 0;
    }

}
