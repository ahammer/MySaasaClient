package com.mysassa.api;

import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.responses.GetBlogCommentsResponse;
import com.mysassa.api.responses.PostCommentResponse;
import com.mysassa.api.responses.PostReplyResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Adam on 1/6/2015.
 */
public class CommentManager {
    private final MySaasaClient mySaasaClient;
    public Map<Long, List<BlogComment>> comments = new HashMap<Long, List<BlogComment>>();
    public Map<Long, List<BlogComment>> toplevel_comments = new HashMap<Long, List<BlogComment>>();
    public Map<Long, BlogComment> id_lookup = new HashMap<Long, BlogComment>();
    public Map<BlogComment, List<BlogComment>> child_lookup = new HashMap<BlogComment, List<BlogComment>>();

    public CommentManager(MySaasaClient mySaasaClient) {
        this.mySaasaClient = mySaasaClient;
     //   mySaasaClient.bus.register(this);
    }


    public Observable<BlogComment> getBlogCommentsObservable(BlogPost post) {
        checkNotNull(post);
        final BlogPost post1 = post;
        final MySaasaGateway gateway1 = mySaasaClient.gateway;
        Observable<BlogComment> observable = Observable.create(new Observable.OnSubscribe<BlogComment>() {
            private final MySaasaGateway gateway = gateway1;
            private final BlogPost post = post1;

            @Override
            public void call(Subscriber<? super BlogComment> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<GetBlogCommentsResponse> call = gateway.getBlogComments(post.id, 100);
                    try {
                        Response<GetBlogCommentsResponse> response = call.execute();

                        //Register ID's
                        for (BlogComment bc : response.body().getData()) {
                            id_lookup.put(bc.getId(), bc);
                        }

                        //Lookup children and link
                        for (BlogComment bc : response.body().getData()) {
                            BlogComment parent;

                            //If we have a parent
                            if ((parent = bc.getParent(CommentManager.this)) != null) {
                                //Get the ChildLookup array or initialize for this parent
                                List<BlogComment> children = child_lookup.get(parent);
                                if (children == null) {
                                    children = new ArrayList<BlogComment>();
                                    child_lookup.put(bc, children);
                                }

                                //Register this child in the list
                                if (!children.contains(bc)) {
                                    children.add(bc);
                                }
                            }
                        }


                        for (BlogComment bc : response.body().getData()) {
                            if (bc.calculateDepth(CommentManager.this) == 0) {
                                subscriber.onNext(bc);
                            }
                        }

                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
        return observable.subscribeOn(Schedulers.io());
    }

    public Observable<PostCommentResponse> postBlogComment(final BlogPost post, final String text) {
        Observable<PostCommentResponse> observable = Observable.create(new Observable.OnSubscribe<PostCommentResponse>() {
            @Override
            public void call(Subscriber<? super PostCommentResponse> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<PostCommentResponse> call = mySaasaClient.gateway.postComment(post.id, text);
                    try {
                        Response<PostCommentResponse> response = call.execute();
                        subscriber.onNext(response.body());
                        subscriber.onCompleted();
                        System.out.println(response.toString());
                    } catch (IOException e) {
                        subscriber.onError(e);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
        return observable.subscribeOn(Schedulers.io());

    }

    public Observable<PostReplyResponse> postCommentResponse(final BlogComment comment, final String text) {
        Observable<PostReplyResponse> observable = Observable.create(new Observable.OnSubscribe<PostReplyResponse>() {
            @Override
            public void call(Subscriber<? super PostReplyResponse> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Call<PostReplyResponse> call = mySaasaClient.gateway.postReply(comment.getId(), text);
                    try {
                        Response<PostReplyResponse> response = call.execute();
                        subscriber.onNext(response.body());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
        return observable.subscribeOn(Schedulers.io());

    }

    public BlogComment lookupCommentById(long parent_id) {
        return id_lookup.get(parent_id);
    }

    public List<BlogComment> getChildren(BlogComment blogComment) {
        List<BlogComment> comments = child_lookup.get(blogComment);
        return comments==null?Collections.EMPTY_LIST:comments;
    }
}
