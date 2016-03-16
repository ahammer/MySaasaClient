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

/**
 * Created by Adam on 1/6/2015.
 */
public class CommentManager {
    private final MySaasaClient mySaasaClient;
    public Map<Long, List<BlogComment>> comments = new HashMap<Long, List<BlogComment>>();
    public Map<Long, List<BlogComment>> toplevel_comments = new HashMap<Long, List<BlogComment>>();
    public Map<Long, BlogComment> id_lookup = new HashMap<Long, BlogComment>();

    public CommentManager(MySaasaClient mySaasaClient) {
        this.mySaasaClient = mySaasaClient;
     //   mySaasaClient.bus.register(this);
    }


    public Observable<BlogComment> getBlogCommentsObservable(BlogPost post) {
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
                        registerComments(post.id, response.body().getData());
                        List<BlogComment> normalized = processTopLevelComments(response.body().getData());
                        for (BlogComment bc:normalized)
                            subscriber.onNext(bc);
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
                    Call<PostReplyResponse> call = mySaasaClient.gateway.postReply(comment.id, text);
                    try {
                        Response<PostReplyResponse> response = call.execute();
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

    public void registerComments(long post, List<BlogComment> comments) {
        List<BlogComment> blogComments = CommentManager.this.comments.get(post);
        List<BlogComment> toplevelBlogComments = CommentManager.this.toplevel_comments.get(post);
        if (blogComments == null) {
            blogComments = new ArrayList<BlogComment>();
        }

        if (toplevelBlogComments == null) {
            toplevelBlogComments = new ArrayList<BlogComment>();
        }


        for (BlogComment comment : comments) {
            id_lookup.remove(comment.id);
            id_lookup.put(comment.id, comment);


            if (blogComments.contains(comment)) blogComments.remove(comment);
            blogComments.add(comment);

            if (comment.parent_id==0) {
                if (toplevelBlogComments.contains(comment)) toplevelBlogComments.remove(comment);
                comment.client_visible=true;
                toplevelBlogComments.remove(comment);
                toplevelBlogComments.add(comment);
            }

        }

        CommentManager.this.toplevel_comments.put(post, toplevelBlogComments);
        CommentManager.this.comments.put(post, blogComments);
        scanAndLink();
    }

    private void scanAndLink() {
        for (BlogComment comment:id_lookup.values()) {
            BlogComment bc = id_lookup.get(comment.parent_id);
            if (bc != null) {
                bc.registerChild(comment);
            }
        }
    }

    /**
     * This takes the ones list, and parses it into another list that accounts for the children
     * @param topLevelComments
     * @return
     */
    private List<BlogComment> processTopLevelComments(List<BlogComment> topLevelComments) {
        if (topLevelComments == null || topLevelComments.size() == 0) return Collections.EMPTY_LIST;
        ArrayList<BlogComment> output = new ArrayList<BlogComment>();
        Stack<BlogComment> stk = new Stack();

        for (BlogComment rootNode:topLevelComments) {
            stk.push(rootNode);

            while (!stk.empty()) {
                BlogComment top = stk.pop();
                for (BlogComment child : top.children) {
                    stk.push(child);
                }
                if (top.client_visible) {
                    output.add(top);
                }
            }
        }

        for (BlogComment bc:output) {
            if (bc.parent_id != 0) {
                BlogComment parent = id_lookup.get(bc.parent_id);
                bc.depth = parent.depth+1;
            }
        }
        return output;
    }


    /**
     * This removes it in the client, matching the successful behaviour of the server
     *
     * @param comment
     */
    public void fauxRemove(BlogComment comment) {

        if (comment.children.size() > 0) {
            comment.author = null;
            comment.content = "[DELETED]";
        } else {
            comment.author = null;
            comment.content = "[DELETED]";
            comment.client_visible = false;
        }
    }
}
