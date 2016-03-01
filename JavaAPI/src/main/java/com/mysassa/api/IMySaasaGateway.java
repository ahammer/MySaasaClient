package com.mysassa.api;

import com.mysassa.api.responses.GetBlogPostsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * IMySaasaGateway, Retrofit
 *
 * Created by adam on 2014-09-29.
 */
public interface IMySaasaGateway {
    @FormUrlEncoded
    @POST("BlogApiService/postToBlog")
    PostToBlogResponse postToBlog(@Field("title")String title, @Field("subtitle")String subtitle, @Field("summary")String summary, @Field("body")String body, @Field("category")String category);


    @FormUrlEncoded
    @POST("MessagingApiService/getThread")
    GetThreadResponse getThread(@Field("message_id")long message_id);


    @FormUrlEncoded
    @POST("BlogApiService/getBlogPostById")
    GetBlogPostByIdResponse getBlogPostById(@Field("id")long id);


    @FormUrlEncoded
    @POST("UserApiService/createUser")
    CreateUserResponse createUser(@Field("identifier")String identifier, @Field("password")String password);


    @FormUrlEncoded
    @POST("UserApiService/Website")
    WebsiteResponse Website();


    @FormUrlEncoded
    @POST("BlogApiService/deleteComment")
    DeleteCommentResponse deleteComment(@Field("comment_id")long comment_id);


    @FormUrlEncoded
    @POST("BlogApiService/postReply")
    PostReplyResponse postReply(@Field("parent_comment_id")long parent_comment_id, @Field("comment")String comment);


    @FormUrlEncoded
    @POST("UserApiService/WebsiteTest")
    WebsiteTestResponse WebsiteTest();


    @FormUrlEncoded
    @POST("UserApiService/getSession")
    GetSessionResponse getSession();


    @FormUrlEncoded
    @POST("MessagingApiService/replyMessage")
    ReplyMessageResponse replyMessage(@Field("message_id")long message_id, @Field("message")String message);


    @FormUrlEncoded
    @POST("BlogApiService/deleteBlogPost")
    DeleteBlogPostResponse deleteBlogPost(@Field("post_id")long post_id);


    @FormUrlEncoded
    @POST("MediaApiServiceImpl/getAllMedia")
    GetAllMediaResponse getAllMedia();


    @FormUrlEncoded
    @POST("CategoryApiService/getProductCategories")
    GetProductCategoriesResponse getProductCategories();


    @FormUrlEncoded
    @POST("MessagingApiService/getMessages")
    GetMessagesResponse getMessages(@Field("page")long page, @Field("page_size")long page_size, @Field("order")String order, @Field("direction")String direction);


    @FormUrlEncoded
    @POST("MessagingApiService/getMessageCount")
    GetMessageCountResponse getMessageCount();


    @FormUrlEncoded
    @POST("CategoryApiService/getBlogCategories")
    GetBlogCategoriesResponse getBlogCategories();


    @FormUrlEncoded
    @POST("UserApiService/loginUser")
    LoginUserResponse loginUser(@Field("identifier")String identifier, @Field("password")String password);


    @FormUrlEncoded
    @POST("UserApiService/Media")
    MediaResponse Media();


    @FormUrlEncoded
    @POST("BlogApiService/getBlogComments")
    GetBlogCommentsResponse getBlogComments(@Field("id")long id, @Field("count")int count);


    @FormUrlEncoded
    @POST("UserApiService/ping")
    PingResponse ping();


    @FormUrlEncoded
    @POST("UserApiService/registerGcmKey")
    RegisterGcmKeyResponse registerGcmKey(@Field("gc_reg_id")String gc_reg_id);


    @FormUrlEncoded
    @POST("UserApiService/BasicTest")
    BasicTestResponse BasicTest();


    @FormUrlEncoded
    @POST("BlogApiService/getBlogPosts")
    Call<GetBlogPostsResponse> getBlogPosts(@Field("category")String category, @Field("page")int page, @Field("take")int take, @Field("order")String order, @Field("direction")String direction);



    @FormUrlEncoded
    @POST("MessagingApiService/sendMessage")
    SendMessageResponse sendMessage(@Field("to_user")String to_user, @Field("title")String title, @Field("body")String body, @Field("name")String name, @Field("email")String email, @Field("phone")String phone);


    @FormUrlEncoded
    @POST("UserApiService/logout")
    LogoutResponse logout();


    @FormUrlEncoded
    @POST("BlogApiService/getTopLevelBlogComments")
    GetTopLevelBlogCommentsResponse getTopLevelBlogComments(@Field("id")long id, @Field("count")int count);


    @FormUrlEncoded
    @POST("BlogApiService/postComment")
    PostCommentResponse postComment(@Field("post_id")long post_id, @Field("comment")String comment);


    @FormUrlEncoded
    @POST("UserApiService/AddTwo")
    AddTwoResponse AddTwo(@Field("a")int a, @Field("b")int b);


    @FormUrlEncoded
    @POST("BlogApiService/updateBlogPost")
    UpdateBlogPostResponse updateBlogPost(@Field("id")long id, @Field("title")String title, @Field("subtitle")String subtitle, @Field("summary")String summary, @Field("body")String body);


    @FormUrlEncoded
    @POST("BlogApiService/updateComment")
    UpdateCommentResponse updateComment(@Field("comment_id")long comment_id, @Field("comment")String comment);
}
