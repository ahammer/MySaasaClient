package com.mysassa.api;

import com.mysassa.api.responses.AddTwoResponse;
import com.mysassa.api.responses.BasicTestResponse;
import com.mysassa.api.responses.CreateUserResponse;
import com.mysassa.api.responses.DeleteBlogPostResponse;
import com.mysassa.api.responses.DeleteCommentResponse;
import com.mysassa.api.responses.GetAllMediaResponse;
import com.mysassa.api.responses.GetBlogCategoriesResponse;
import com.mysassa.api.responses.GetBlogCommentsResponse;
import com.mysassa.api.responses.GetBlogPostByIdResponse;
import com.mysassa.api.responses.GetBlogPostsResponse;
import com.mysassa.api.responses.GetMessageCountResponse;
import com.mysassa.api.responses.GetMessagesResponse;
import com.mysassa.api.responses.GetProductCategoriesResponse;
import com.mysassa.api.responses.GetSessionResponse;
import com.mysassa.api.responses.GetThreadResponse;
import com.mysassa.api.responses.GetTopLevelBlogCommentsResponse;
import com.mysassa.api.responses.LoginUserResponse;
import com.mysassa.api.responses.LogoutResponse;
import com.mysassa.api.responses.MediaResponse;
import com.mysassa.api.responses.PingResponse;
import com.mysassa.api.responses.PostCommentResponse;
import com.mysassa.api.responses.PostReplyResponse;
import com.mysassa.api.responses.PostToBlogResponse;
import com.mysassa.api.responses.RegisterGcmKeyResponse;
import com.mysassa.api.responses.ReplyMessageResponse;
import com.mysassa.api.responses.SendMessageResponse;
import com.mysassa.api.responses.UpdateBlogPostResponse;
import com.mysassa.api.responses.UpdateCommentResponse;
import com.mysassa.api.responses.WebsiteResponse;
import com.mysassa.api.responses.WebsiteTestResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * MySaasaGateway, Retrofit
 *
 * Created by adam on 2014-09-29.
 */
public interface MySaasaGateway {
    @FormUrlEncoded
    @POST("BlogApiService/postToBlog")
    Call<PostToBlogResponse> postToBlog(@Field("title")String title, @Field("subtitle")String subtitle, @Field("summary")String summary, @Field("body")String body, @Field("category")String category);


    @FormUrlEncoded
    @POST("MessagingApiService/getThread")
    Call<GetThreadResponse> getThread(@Field("message_id")long message_id);


    @FormUrlEncoded
    @POST("BlogApiService/getBlogPostById")
    Call<GetBlogPostByIdResponse> getBlogPostById(@Field("id")long id);


    @FormUrlEncoded
    @POST("UserApiService/createUser")
    Call<CreateUserResponse> createUser(@Field("identifier")String identifier, @Field("password")String password);


    @FormUrlEncoded
    @POST("UserApiService/Website")
    Call<WebsiteResponse> Website();


    @FormUrlEncoded
    @POST("BlogApiService/deleteComment")
    Call<DeleteCommentResponse> deleteComment(@Field("comment_id")long comment_id);


    @FormUrlEncoded
    @POST("BlogApiService/postReply")
    Call<PostReplyResponse> postReply(@Field("parent_comment_id")long parent_comment_id, @Field("comment")String comment);


    @FormUrlEncoded
    @POST("UserApiService/WebsiteTest")
    Call<WebsiteTestResponse> WebsiteTest();


    @FormUrlEncoded
    @POST("UserApiService/getSession")
    Call<GetSessionResponse> getSession();


    @FormUrlEncoded
    @POST("MessagingApiService/replyMessage")
    Call<ReplyMessageResponse> replyMessage(@Field("message_id")long message_id, @Field("message")String message);


    @FormUrlEncoded
    @POST("BlogApiService/deleteBlogPost")
    Call<DeleteBlogPostResponse> deleteBlogPost(@Field("post_id")long post_id);


    @FormUrlEncoded
    @POST("MediaApiServiceImpl/getAllMedia")
    Call<GetAllMediaResponse> getAllMedia();


    @FormUrlEncoded
    @POST("CategoryApiService/getProductCategories")
    Call<GetProductCategoriesResponse> getProductCategories();


    @FormUrlEncoded
    @POST("MessagingApiService/getMessages")
    Call<GetMessagesResponse> getMessages(@Field("page")long page, @Field("page_size")long page_size, @Field("order")String order, @Field("direction")String direction);



    @GET("MessagingApiService/getMessageCount")
    Call<GetMessageCountResponse> getMessageCount();


    @POST("CategoryApiService/getBlogCategories")
    Call<GetBlogCategoriesResponse> getBlogCategories();


    @FormUrlEncoded
    @POST("UserApiService/loginUser")
    Call<LoginUserResponse> loginUser(@Field("identifier")String identifier, @Field("password")String password);


    @FormUrlEncoded
    @POST("UserApiService/Media")
    Call<MediaResponse> Media();


    @FormUrlEncoded
    @POST("BlogApiService/getBlogComments")
    Call<GetBlogCommentsResponse> getBlogComments(@Field("post_id")long id, @Field("count")int count);


    @FormUrlEncoded
    @POST("UserApiService/ping")
    Call<PingResponse> ping();


    @FormUrlEncoded
    @POST("UserApiService/registerGcmKey")
    Call<RegisterGcmKeyResponse> registerGcmKey(@Field("gc_reg_id")String gc_reg_id);


    @FormUrlEncoded
    @POST("UserApiService/BasicTest")
    Call<BasicTestResponse> BasicTest();


    @FormUrlEncoded
    @POST("BlogApiService/getBlogPosts")
    Call<GetBlogPostsResponse> getBlogPosts(@Field("category")String category, @Field("page")int page, @Field("take")int take, @Field("order")String order, @Field("direction")String direction);


    @FormUrlEncoded
    @POST("MessagingApiService/sendMessage")
    Call<SendMessageResponse> sendMessage(@Field("to_user")String to_user, @Field("title")String title, @Field("body")String body, @Field("name")String name, @Field("email")String email, @Field("phone")String phone);


    @FormUrlEncoded
    @POST("UserApiService/logout")
    Call<LogoutResponse> logout();


    @FormUrlEncoded
    @POST("BlogApiService/getTopLevelBlogComments")
    Call<GetTopLevelBlogCommentsResponse> getTopLevelBlogComments(@Field("id")long id, @Field("count")int count);


    @FormUrlEncoded
    @POST("BlogApiService/postComment")
    Call<PostCommentResponse> postComment(@Field("post_id")long post_id, @Field("comment")String comment);


    @FormUrlEncoded
    @POST("UserApiService/AddTwo")
    Call<AddTwoResponse> AddTwo(@Field("a")int a, @Field("b")int b);


    @FormUrlEncoded
    @POST("BlogApiService/updateBlogPost")
    Call<UpdateBlogPostResponse> updateBlogPost(@Field("id")long id, @Field("title")String title, @Field("subtitle")String subtitle, @Field("summary")String summary, @Field("body")String body);


    @FormUrlEncoded
    @POST("BlogApiService/updateComment")
    Call<UpdateCommentResponse> updateComment(@Field("comment_id")long comment_id, @Field("comment")String comment);
}
