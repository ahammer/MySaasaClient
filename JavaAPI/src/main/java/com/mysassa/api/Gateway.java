package com.mysassa.api;

import com.google.gson.JsonParser;
import com.mysassa.NotAuthorizedException;
import com.mysassa.api.responses.SimpleResponse;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Gateway to My API, basic calls, close to the metal, no threading
 *
 * Created by adam on 2014-09-29.
 */
public class Gateway {
    final HttpClient httpClient = HttpClients.createSystem();
    final HttpHost domain;
    final JsonParser parser = new JsonParser();
    public int port;
    //final Gson gson = new Gson();
    //final int port;

    public Gateway(String root, int port, String scheme) {
        domain = new HttpHost(root,port, scheme);
        this.port = port;
    }

    public static  enum API_FUNCTIONS {BlogApiService_postToBlog, MessagingApiService_getThread, BlogApiService_getBlogPostById, UserApiService_createUser, UserApiService_Website, BlogApiService_deleteComment, BlogApiService_postReply, UserApiService_WebsiteTest, UserApiService_getSession, MessagingApiService_replyMessage, BlogApiService_deleteBlogPost, MediaApiServiceImpl_getAllMedia, CategoryApiService_getProductCategories, MessagingApiService_getMessages, MessagingApiService_getMessageCount, CategoryApiService_getBlogCategories, UserApiService_loginUser, UserApiService_Media, BlogApiService_getBlogComments, UserApiService_ping, UserApiService_registerGcmKey, BlogApiService_getBlogPosts, UserApiService_BasicTest, MessagingApiService_sendMessage, UserApiService_logout, BlogApiService_getTopLevelBlogComments, BlogApiService_postComment, BlogApiService_updateBlogPost, UserApiService_AddTwo, BlogApiService_updateComment};
    public SimpleResponse BlogApiService_postToBlog(String title, String subtitle, String summary, String body, String category) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/postToBlog";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("title", title.trim()));
        formparams.add(new BasicNameValuePair("subtitle", subtitle.trim()));
        formparams.add(new BasicNameValuePair("summary", summary.trim()));
        formparams.add(new BasicNameValuePair("body", body.trim()));
        formparams.add(new BasicNameValuePair("category", category.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_postToBlog);

        return simpleResponse;

    }
    public SimpleResponse MessagingApiService_getThread(long message_id) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/getThread";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("message_id", String.valueOf(message_id)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.MessagingApiService_getThread);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_getBlogPostById(long id) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getBlogPostById";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("id", String.valueOf(id)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_getBlogPostById);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_createUser(String identifier, String password) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/createUser";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("identifier", identifier.trim()));
        formparams.add(new BasicNameValuePair("password", password.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_createUser);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_Website() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/Website";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_Website);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_deleteComment(long comment_id) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/deleteComment";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("comment_id", String.valueOf(comment_id)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_deleteComment);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_postReply(long parent_comment_id, String comment) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/postReply";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("parent_comment_id", String.valueOf(parent_comment_id)));
        formparams.add(new BasicNameValuePair("comment", comment.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_postReply);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_WebsiteTest() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/WebsiteTest";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_WebsiteTest);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_getSession() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/getSession";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_getSession);

        return simpleResponse;

    }
    public SimpleResponse MessagingApiService_replyMessage(long message_id, String message) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/replyMessage";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("message_id", String.valueOf(message_id)));
        formparams.add(new BasicNameValuePair("message", message.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.MessagingApiService_replyMessage);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_deleteBlogPost(long post_id) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/deleteBlogPost";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("post_id", String.valueOf(post_id)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_deleteBlogPost);

        return simpleResponse;

    }
    public SimpleResponse MediaApiServiceImpl_getAllMedia() throws IOException, NotAuthorizedException
    {
        String path = "/MediaApiServiceImpl/getAllMedia";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.MediaApiServiceImpl_getAllMedia);

        return simpleResponse;

    }
    public SimpleResponse CategoryApiService_getProductCategories() throws IOException, NotAuthorizedException
    {
        String path = "/CategoryApiService/getProductCategories";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.CategoryApiService_getProductCategories);

        return simpleResponse;

    }
    public SimpleResponse MessagingApiService_getMessages(long page, long page_size, String order, String direction) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/getMessages";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("page", String.valueOf(page)));
        formparams.add(new BasicNameValuePair("page_size", String.valueOf(page_size)));
        formparams.add(new BasicNameValuePair("order", order.trim()));
        formparams.add(new BasicNameValuePair("direction", direction.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.MessagingApiService_getMessages);

        return simpleResponse;

    }
    public SimpleResponse MessagingApiService_getMessageCount() throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/getMessageCount";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.MessagingApiService_getMessageCount);

        return simpleResponse;

    }
    public SimpleResponse CategoryApiService_getBlogCategories() throws IOException, NotAuthorizedException
    {
        String path = "/CategoryApiService/getBlogCategories";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.CategoryApiService_getBlogCategories);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_loginUser(String identifier, String password) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/loginUser";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("identifier", identifier.trim()));
        formparams.add(new BasicNameValuePair("password", password.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_loginUser);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_Media() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/Media";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_Media);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_getBlogComments(long id, int count) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getBlogComments";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("id", String.valueOf(id)));
        formparams.add(new BasicNameValuePair("count", String.valueOf(count)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_getBlogComments);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_ping() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/ping";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_ping);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_registerGcmKey(String gc_reg_id) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/registerGcmKey";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("gc_reg_id", gc_reg_id.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_registerGcmKey);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_getBlogPosts(String category, int page, int take, String order, String direction) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getBlogPosts";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("category", category.trim()));
        formparams.add(new BasicNameValuePair("page", String.valueOf(page)));
        formparams.add(new BasicNameValuePair("take", String.valueOf(take)));
        formparams.add(new BasicNameValuePair("order", order.trim()));
        formparams.add(new BasicNameValuePair("direction", direction.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_getBlogPosts);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_BasicTest() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/BasicTest";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_BasicTest);

        return simpleResponse;

    }
    public SimpleResponse MessagingApiService_sendMessage(String to_user, String title, String body, String name, String email, String phone) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/sendMessage";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("to_user", to_user.trim()));
        formparams.add(new BasicNameValuePair("title", title.trim()));
        formparams.add(new BasicNameValuePair("body", body.trim()));
        formparams.add(new BasicNameValuePair("name", name.trim()));
        formparams.add(new BasicNameValuePair("email", email.trim()));
        formparams.add(new BasicNameValuePair("phone", phone.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.MessagingApiService_sendMessage);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_logout() throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/logout";
        HttpPost post = new HttpPost(domain  + path);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_logout);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_getTopLevelBlogComments(long id, int count) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getTopLevelBlogComments";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("id", String.valueOf(id)));
        formparams.add(new BasicNameValuePair("count", String.valueOf(count)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_getTopLevelBlogComments);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_postComment(long post_id, String comment) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/postComment";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("post_id", String.valueOf(post_id)));
        formparams.add(new BasicNameValuePair("comment", comment.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_postComment);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_updateBlogPost(long id, String title, String subtitle, String summary, String body) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/updateBlogPost";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("id", String.valueOf(id)));
        formparams.add(new BasicNameValuePair("title", title.trim()));
        formparams.add(new BasicNameValuePair("subtitle", subtitle.trim()));
        formparams.add(new BasicNameValuePair("summary", summary.trim()));
        formparams.add(new BasicNameValuePair("body", body.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_updateBlogPost);

        return simpleResponse;

    }
    public SimpleResponse UserApiService_AddTwo(int a, int b) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/AddTwo";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("a", String.valueOf(a)));
        formparams.add(new BasicNameValuePair("b", String.valueOf(b)));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.UserApiService_AddTwo);

        return simpleResponse;

    }
    public SimpleResponse BlogApiService_updateComment(long comment_id, String comment) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/updateComment";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("comment_id", String.valueOf(comment_id)));
        formparams.add(new BasicNameValuePair("comment", comment.trim()));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding!!");
        }
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        SimpleResponse simpleResponse = SimpleResponse.create(parser.parse(responseBody).getAsJsonObject(), API_FUNCTIONS.BlogApiService_updateComment);

        return simpleResponse;

    }
}
