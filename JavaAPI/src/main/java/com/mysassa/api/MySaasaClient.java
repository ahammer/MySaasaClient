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
    final IMySaasaGateway gateway;

    //We use Guava event bus sometimes
    public final EventBus bus = new EventBus();

    private final BlogManager blogManager;
    private final LoginManager loginManager;
    private final MessageManager messagesManager;
    private final CategoryManager categoryManager;


    //Observables





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

        categoryManager = new CategoryManager(this);
        blogManager = new BlogManager(this);
        loginManager = new LoginManager(this);
        messagesManager = new MessageManager(this);
    }

    public BlogManager getBlogManager() {
        return blogManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public MessageManager getMessagesManager() {
        return messagesManager;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }






}
