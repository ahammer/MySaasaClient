package com.mysassa.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysassa.api.enums.NetworkState;
import com.mysassa.api.messages.MessageCountUpdated;
import com.mysassa.api.messages.NetworkStateChange;
import com.mysassa.api.messages.ServiceSigninMessage;
import com.mysassa.api.messages.SigninMessage;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.model.Message;
import com.mysassa.api.responses.GetBlogPostsResponse;
import com.mysassa.api.responses.GetCategoriesResponse;
import com.mysassa.api.rx.RxBus;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
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
public class Service {

    public final RxBus bus = new RxBus();
    private final IMySaasaGateway gateway;

    private final ScheduledExecutorService autoScheduler = Executors.newScheduledThreadPool(1);
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final ServiceState state = new ServiceState();
    private Gson gson = new GsonBuilder().create();
    public CommentManager mCommentManager;
    public BlogPostManager mBlogPostManager = new BlogPostManager();
    /**
     * Refresh Policy definitions?
     */

    Observable<Category> categoryObservable = Observable.create(new Observable.OnSubscribe<Category>() {
        @Override
        public void call(Subscriber<? super Category> subscriber) {
            if (!subscriber.isUnsubscribed()) {
                GetBlogCategoriesResponse results = (GetBlogCategoriesResponse) gateway.getBlogCategories();
                    for (Category c:results.results) subscriber.onNext(c);
                subscriber.onCompleted();
            }
        }
    });

    public Observable<BlogPost> getBlogPostsObservable(final Category c) {
        return Observable.create(new Observable.OnSubscribe<BlogPost>() {
            @Override
            public void call(Subscriber<? super BlogPost> subscriber) {

                if (!subscriber.isUnsubscribed()) {

                    Call<GetBlogPostsResponse> call = gateway.getBlogPosts(c.name, 0, 100, "priority", "DESC");
                    try {
                        Response<GetBlogPostsResponse> response = call.execute();
                        for (BlogPost bp : response.body().getData()) {
                            subscriber.onNext(bp);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Category> getCategoryObservable() {
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
    public Service(String domain, int port, String scheme) {
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

    public ServiceState getState() {
        return state;
    }


    private void getMessages(final long Page, final long Page_Size, final String order, final String direction) {

    }

    /**
     * Request the thread for this message
     * @param rootMessage
     */
    public void requestMessageThread(final Message rootMessage) {

    }

    public long sendMessage(final String to, final String title, final String body, final String name, final String email, final String phone) {
        return 0l;
    }



    public void getMessageCount() {

    }

    /**
     * Log the user in. Will not execute if already in progress,
     *
     * @param username
     * @param password
     */
    public void login(final String username, final String password) {

    }

    public void registerGcmSenderId(String gcm_sender_id) {
        state.gcm_sender_id = gcm_sender_id;
        sendGcmSenderId(state.gcm_sender_id);
    }

    private void sendGcmSenderId(final String gcm_sender_id) {
/*

        final RefreshPolicy rp = getRefreshPolicity(IMySaasaGateway.API_FUNCTIONS.UserApiService_registerGcmKey);

        if (state.isAuthenticated() && gcm_sender_id != null && !state.gcm_sent) {
            synchronized (rp) {
                if (rp.locked) return;
                rp.setLocked(true);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SimpleResponse result = gateway.UserApiService_registerGcmKey(gcm_sender_id);

                            state.gcm_sent = true;


                        } catch (IOException e) {
                            bus.post(new ErrorMessage(e));
                        } catch (NotAuthorizedException e) {
                            e.printStackTrace();
                        }
                        rp.setLocked(false);
                    }
                });
            }
        }
        */
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

    void retrieveBlogCategories() {

    }

    void retrieveBlogPosts(final Category category, final int page, final int count, final String order, final String direction) {
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

}
