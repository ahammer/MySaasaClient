package com.mysassa.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysassa.NotAuthorizedException;
import com.mysassa.api.enums.NetworkState;
import com.mysassa.api.messages.BlogCategoriesReceivedMessage;
import com.mysassa.api.messages.BlogCommentsRetrievedMessage;
import com.mysassa.api.messages.BlogPostsModified;
import com.mysassa.api.messages.DeletedBlogComment;
import com.mysassa.api.messages.MessageCountUpdated;
import com.mysassa.api.messages.MessagesUpdated;
import com.mysassa.api.messages.NetworkStateChange;
import com.mysassa.api.messages.ProductCategoriesUpdatedMessage;
import com.mysassa.api.messages.ServiceSigninMessage;
import com.mysassa.api.messages.SigninMessage;
import com.mysassa.api.messages.SignoutMessage;
import com.mysassa.api.messages.ThreadUpdated;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;
import com.mysassa.api.model.Media;
import com.mysassa.api.model.Message;
import com.mysassa.api.model.User;
import com.mysassa.api.responses.BlogApiService_getBlogCommentsResponse;
import com.mysassa.api.responses.BlogApiService_getBlogPostsResponse;
import com.mysassa.api.responses.CategoryService_getCategoriesResponse;
import com.mysassa.api.responses.MessagingApiService_getMessageCountResponse;
import com.mysassa.api.responses.MessagingApiService_getMessagesResponse;
import com.mysassa.api.responses.SimpleResponse;
import com.mysassa.api.responses.UserApiService_loginUserResponse;
import com.mysassa.api.rx.RxBus;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

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
    public final Observable<ServiceState> stateObservable;
    private final ScheduledExecutorService autoScheduler = Executors.newScheduledThreadPool(1);
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Gateway gateway;
    private final ServiceState state = new ServiceState();
    private Gson gson = new GsonBuilder().create();
    public CommentManager mCommentManager;
    public BlogPostManager mBlogPostManager = new BlogPostManager();
    /**
     * Refresh Policy definitions?
     */
    private Map<Gateway.API_FUNCTIONS, RefreshPolicy> refreshPolicies = new HashMap();

    Observable<Category> categoryObservable = Observable.create(new Observable.OnSubscribe<Category>() {
        @Override
        public void call(Subscriber<? super Category> subscriber) {
            if (!subscriber.isUnsubscribed()) {
                try {
                    CategoryService_getCategoriesResponse  results = (CategoryService_getCategoriesResponse) gateway.CategoryApiService_getBlogCategories();
                    for (Category c:results.results) subscriber.onNext(c);
                } catch (IOException e) {
                    subscriber.onError(e);
                } catch (NotAuthorizedException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }
    });

    public Observable<BlogPost> getBlogPostsObservable(final Category c) {
        return Observable.create(new Observable.OnSubscribe<BlogPost>() {
            @Override
            public void call(Subscriber<? super BlogPost> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        for (BlogPost bp : ((BlogApiService_getBlogPostsResponse) gateway.BlogApiService_getBlogPosts(c.name, 0, 100, "priority", "DESC")).results) {
                            subscriber.onNext(bp);
                        }
                    } catch (IOException e) {
                        subscriber.onError(e);
                    } catch (NotAuthorizedException e) {
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
        if (domain == null) throw new IllegalArgumentException("Can not have a null domain");
        bus.toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                handleMessage(o);
            }
        });

        stateObservable = Observable.just(state);
        gateway = new Gateway(domain, port,scheme);
        mCommentManager = new CommentManager(this);

        autoScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    if (state.authenticated) {
                        System.out.println("Keeping alive");
                        gateway.UserApiService_ping();
                    }
                } catch (IOException e) {
                    bus.post(new ErrorMessage(e));
                    e.printStackTrace();
                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }
            }
        }, 120, TimeUnit.SECONDS);
        getProductCategories();
        getProducts();
    }

    public ServiceState getState() {
        return state;
    }

    /**
     * This checks all the API calls and their locks, and sees if something is active.
     *
     * @return
     */
    public NetworkState getNetworkState() {
        for (RefreshPolicy rp : refreshPolicies.values()) {
            if (rp.isLocked()) return NetworkState.Working;
        }
        return NetworkState.Idle;
    }

    public Gateway getGateway() {
        return gateway;
    }

    private void handleMessage(Object o) {
        if (o instanceof ServiceSigninMessage) {
            ServiceSigninMessage msg = (ServiceSigninMessage) o;
            if (((ServiceSigninMessage) o).isSuccess()) {
                state.authenticated = true;
                sendGcmSenderId(state.gcm_sender_id);
                getMessageCount();
            }
            bus.post(new SigninMessage(msg.response));

        } else if (o instanceof MessageCountUpdated) {
            updateMessages();
        }
    }



    public void updateMessages() {
        checkNotNull(state.messageCount);
        getMessages(0, state.messageCount, "timeSent", "DESC");
    }

    private void getMessages(final long Page, final long Page_Size, final String order, final String direction) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.MessagingApiService_getMessages);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        MessagingApiService_getMessagesResponse result = (MessagingApiService_getMessagesResponse) gateway.MessagingApiService_getMessages(Page, Page_Size, order, direction);
                        if (result.isSuccess()) {
                            state.messages.addRootMessages(result.messages);
                        } else {

                        }
                        bus.post(new MessagesUpdated(result));
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

    /**
     * Request the thread for this message
     * @param rootMessage
     */
    public void requestMessageThread(final Message rootMessage) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.MessagingApiService_getThread);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        MessagingApiService_getMessagesResponse result = (MessagingApiService_getMessagesResponse) gateway.MessagingApiService_getThread(rootMessage.id);
                        state.messages.refreshThread(result.messages);
                        bus.post(new ThreadUpdated(result, rootMessage));
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

    public long sendMessage(final String to, final String title, final String body, final String name, final String email, final String phone) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.MessagingApiService_sendMessage);
        synchronized (rp) {
            if (rp.locked) return 0;
            final long requestCode = rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SimpleResponse result = gateway.MessagingApiService_sendMessage(to, title, body, name, email, phone);
                        bus.post(new MessageSuccessfullySent(result,requestCode));
                        //bus.post(new MessagesUpdated(result));
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        e.printStackTrace();
                    }
                    rp.setLocked(false);
                }
            });
            return requestCode;
        }
    }


    public void getProductCategories() {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.CategoryApiService_getProductCategories);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        CategoryService_getCategoriesResponse result = (CategoryService_getCategoriesResponse) gateway.CategoryApiService_getProductCategories();
                        state.product_categories = result.results;
                        bus.post(new ProductCategoriesUpdatedMessage(result));
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

    public void getProducts() {
        /*
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.InventoryApiService_getProducts);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        InventoryApiService_getProductsResponse result = (InventoryApiService_getProductsResponse) gateway.InventoryApiService_getProducts();
                        state.products = result.getProducts();
                        bus.post(new ProductsRetrievedMessage(result));
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        e.printStackTrace();
                    }
                    rp.setLocked(false);
                }
            });
        }
        */
    }

    public void getMessageCount() {

        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.MessagingApiService_getMessageCount);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        MessagingApiService_getMessageCountResponse result = (MessagingApiService_getMessageCountResponse) gateway.MessagingApiService_getMessageCount();
                        state.messageCount = result.count;
                        bus.post(new MessageCountUpdated(result));
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

    /**
     * Log the user in. Will not execute if already in progress,
     *
     * @param username
     * @param password
     */
    public void login(final String username, final String password) {
        state.current_password = password;
        state.current_username = username;

        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.UserApiService_loginUser);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SimpleResponse result = gateway.UserApiService_loginUser(username, password);
                        state.user = ((UserApiService_loginUserResponse) result).user;
                        bus.post(new ServiceSigninMessage(result));
                    } catch (IOException e) {
                        bus.post(new ServiceSigninMessage(e));
                    } catch (NotAuthorizedException e) {
                        bus.post(new ServiceSigninMessage(e));
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void registerGcmSenderId(String gcm_sender_id) {
        state.gcm_sender_id = gcm_sender_id;
        sendGcmSenderId(state.gcm_sender_id);
    }

    private void sendGcmSenderId(final String gcm_sender_id) {


        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.UserApiService_registerGcmKey);

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
    }


    public void signout() {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.UserApiService_logout);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SimpleResponse result = gateway.UserApiService_logout();
                        state.authenticated = false;
                        state.user = null;
                        bus.post(new SignoutMessage());
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

    public void createUser(final String username, final String password) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.UserApiService_createUser);
        if (rp.locked) return;
        rp.setLocked(true);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    UserApiService_loginUserResponse result = (UserApiService_loginUserResponse) gateway.UserApiService_createUser(username, password);
                    state.user = result.user;
                    bus.post(new ServiceSigninMessage(result));
                } catch (IOException e) {
                    bus.post(new ErrorMessage(e));
                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }

                rp.setLocked(false);
            }
        });
    }

    public void commentOnBlog(final long id, final String comment) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_postComment);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        gateway.BlogApiService_postComment(id, comment);
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            commentOnBlog(id, comment);
                            return;
                        }
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void postToBlog(final String title, final String subtitle, final String summary, final String value, final String category) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_postToBlog);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        gateway.BlogApiService_postToBlog(title, subtitle, summary, value, category);
                        bus.post(new BlogPostsModified());
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            postToBlog(title, subtitle, summary, value, category);
                            return;
                        }
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void updateBlogPost(final BlogPost post, final String title,final String subtitle, final String summary, final String value) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_updateBlogPost);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        gateway.BlogApiService_updateBlogPost(post.id, title, subtitle, summary, value);
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            updateBlogPost(post, title, subtitle, summary, value);
                            return;
                        }
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void updateBlogComment(final BlogComment comment, final String text) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_updateComment);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        gateway.BlogApiService_updateComment(comment.id, text);
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            updateBlogComment(comment, text);
                            return;
                        }
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void replyToComment(final long comment_id, final String comment) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_postReply);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        gateway.BlogApiService_postReply(comment_id, comment);
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            replyToComment(comment_id, comment);
                            return;
                        }
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void deleteBlogComment(final BlogComment comment) {
        checkNotNull(comment);
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_deleteComment);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SimpleResponse result = gateway.BlogApiService_deleteComment(comment.id);
                        if (result.isSuccess()) {
                            mCommentManager.fauxRemove(comment);
                            bus.post(new DeletedBlogComment(comment));
                        }
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            deleteBlogComment(comment);
                            return;
                        }
                    }
                    rp.setLocked(false);
                }
            });
        }
    }

    public void deleteBlogPost(final BlogPost post) {
        checkNotNull(post);
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_deleteBlogPost);
        synchronized (rp) {
            if (rp.locked) return;
            rp.setLocked(true);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SimpleResponse result = gateway.BlogApiService_deleteBlogPost(post.id);
                        if (result.isSuccess()) {
                            mBlogPostManager.remove(post);
                            bus.post(new BlogPostsModified());
                        }
                    } catch (IOException e) {
                        bus.post(new ErrorMessage(e));
                    } catch (NotAuthorizedException e) {
                        if (autoLogin()) {
                            rp.setLocked(false);
                            deleteBlogPost(post);
                            return;
                        }
                    }
                }
            });
            rp.setLocked(false);
        }
    }

    /**
     * Helper to turn a media into a url string
     *
     * @param media
     * @return
     */
    public String getMediaUrl(Media media) {
        String s = gateway.domain + "/media/" + media.uid;
        return s;
    }

    @Deprecated
    public List<Category> getBlogCategories() {
        if (state.blogCategories == Collections.EMPTY_LIST) {
            retrieveBlogCategories();
        }
        return state.blogCategories;
    }

    public List<BlogPost> getBlogPosts(Category category, int page, int count, String order, String direction) {
        if (!mBlogPostManager.hasPreloadedCategory(category)) {
            retrieveBlogPosts(category, page, count, order, direction);
        }

        List l = mBlogPostManager.getBlogPostsForCategory(category);
        if (l!=null) return l;
        return Collections.EMPTY_LIST;
    }

    public List<BlogComment> getBlogComments(long id, int count, boolean force) {
        if (force) {
            retrieveBlogComments(id, count);
        }
        return mCommentManager.getComments(id);
    }

    void retrieveBlogCategories() {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.CategoryApiService_getBlogCategories);
        if (rp.locked) return;
        rp.setLocked(true);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CategoryService_getCategoriesResponse response = (CategoryService_getCategoriesResponse) gateway.CategoryApiService_getBlogCategories();
                    state.blogCategories = response.results;
                    bus.post(new BlogCategoriesReceivedMessage());
                } catch (IOException e) {
                    notifyNetworkWorking(NetworkState.Error);
                    e.printStackTrace();
                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }
                rp.setLocked(false);
            }
        });
    }

    void retrieveBlogPosts(final Category category, final int page, final int count, final String order, final String direction) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_getBlogPosts);
        if (rp.locked) return;
        rp.setLocked(true);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BlogApiService_getBlogPostsResponse response = (BlogApiService_getBlogPostsResponse) gateway.BlogApiService_getBlogPosts(category.name, page, count, order, direction);
                    for (BlogPost p:response.results)
                        mBlogPostManager.addBlogPost(p);

                    bus.post(new BlogPostsModified());
                } catch (IOException e) {
                    notifyNetworkWorking(NetworkState.Error);
                    e.printStackTrace();

                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }
                rp.setLocked(false);
            }
        });
    }

    public void getBlogPostById(final long BlogPostId) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_getBlogPostById);
        if (rp.locked) return;
        rp.setLocked(true);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BlogApiService_getBlogPostsResponse response = (BlogApiService_getBlogPostsResponse) gateway.BlogApiService_getBlogPostById(BlogPostId);
                    for (BlogPost post : response.results) {
                        mBlogPostManager.addBlogPost(post);
                    }
                    bus.post(new BlogPostsModified(response));
                } catch (IOException e) {
                    notifyNetworkWorking(NetworkState.Error);
                    e.printStackTrace();

                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }
                rp.setLocked(false);
            }
        });
    }

    void retrieveBlogComments(final long id, final int count) {
        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.BlogApiService_getBlogComments);
        if (rp.locked) return;
        rp.setLocked(true);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BlogApiService_getBlogCommentsResponse response = (BlogApiService_getBlogCommentsResponse) gateway.BlogApiService_getBlogComments(id, count);
                    if (state.authenticated) {
                        User u = state.user;
                        if (u != null)
                            for (BlogComment comment : response.comments)
                                if (comment.author != null && comment.author.id == u.id)
                                    comment.client_visible = true;
                    }
                    mCommentManager.registerComments(id, response.comments);
                    bus.post(new BlogCommentsRetrievedMessage(id));
                } catch (IOException e) {
                    notifyNetworkWorking(NetworkState.Error);
                    e.printStackTrace();
                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }
                rp.setLocked(false);
            }
        });

    }

    private void notifyNetworkWorking(NetworkState state) {
        bus.post(new NetworkStateChange(state));
    }

    private RefreshPolicy getRefreshPolicity(Gateway.API_FUNCTIONS func) {
        RefreshPolicy policy = refreshPolicies.get(func);
        if (policy == null) {
            policy = new RefreshPolicy(this);
            refreshPolicies.put(func,policy);
        }

        return policy;
    }

    public boolean isExecuting(Gateway.API_FUNCTIONS func) {
        return getRefreshPolicity(func).isLocked();
    }

    /**
     * Private helper to inline-login again if things are invalidated
     *
     * @return
     */
    private boolean autoLogin() {
        try {
            UserApiService_loginUserResponse result = (UserApiService_loginUserResponse) gateway.UserApiService_loginUser(state.current_username, state.current_password);

            if (result.user != null) {
                state.user = ((UserApiService_loginUserResponse) result).user;
                bus.post(new ServiceSigninMessage(result));
                return true;
            }
        } catch (IOException e) {
            bus.post(new ServiceSigninMessage(e));
            e.printStackTrace();
        } catch (NotAuthorizedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent() {
    }


    public BlogPost locateBlogpost(final long blogpost_id) {
        return mBlogPostManager.getBlogPostById(blogpost_id);
    }

    public void replyToMessage(final Message rootMessage, final String s) {

        final RefreshPolicy rp = getRefreshPolicity(Gateway.API_FUNCTIONS.MessagingApiService_replyMessage);
        if (rp.locked) return;
        rp.setLocked(true);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SimpleResponse response =  gateway.MessagingApiService_replyMessage(rootMessage.id, s);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotAuthorizedException e) {
                    e.printStackTrace();
                }
                rp.setLocked(false);
            }
        });

    }


    public static class RefreshPolicy {
        private final Service service;
        int failureCount = 0;
        long lastUpdated = 0;
        long timeoutMillis = 60000;
        private static volatile long sequence = 0;

        volatile boolean locked = false;

        public RefreshPolicy(Service s) {
            this.service = s;
        }

        public boolean shouldUpdate() {
            long delta = System.currentTimeMillis() - lastUpdated;
            return (delta > timeoutMillis);
        }

        public void reset() {
            lastUpdated = System.currentTimeMillis();
            failureCount++;
        }

        public boolean isLocked() {
            return locked;
        }

        public long setLocked(boolean locked) {
            if (locked == this.locked)
                throw new IllegalStateException("Double lock or unlock: " + this.locked);
            this.locked = locked;
            service.notifyNetworkWorking(service.getNetworkState());
            if (locked) return sequence++;
            return sequence;
        }

    }

}
