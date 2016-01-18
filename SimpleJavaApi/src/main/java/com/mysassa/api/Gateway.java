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

    //Generated code
    public static  enum API_FUNCTIONS {BlogApiService_postToBlog, MessagingApiService_getThread, BlogApiService_getBlogPostById, UserApiService_createUser, UserApiService_Website, BlogApiService_deleteComment, BlogApiService_postReply, UserApiService_WebsiteTest, UserApiService_getSession, MessagingApiService_replyMessage, BlogApiService_deleteBlogPost, MediaApiServiceImpl_getAllMedia, CategoryApiService_getProductCategories, MessagingApiService_getMessageCount, MessagingApiService_getMessages, CategoryApiService_getBlogCategories, UserApiService_loginUser, UserApiService_Media, BlogApiService_getBlogComments, UserApiService_ping, UserApiService_registerGcmKey, UserApiService_BasicTest, BlogApiService_getBlogPosts, MessagingApiService_sendMessage, UserApiService_logout, BlogApiService_getTopLevelBlogComments, BlogApiService_postComment, UserApiService_AddTwo, BlogApiService_updateBlogPost, BlogApiService_updateComment};
    public SimpleResponse BlogApiService_postToBlog(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/postToBlog";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", arg0.trim()));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
        formparams.add(new BasicNameValuePair("arg2", arg2.trim()));
        formparams.add(new BasicNameValuePair("arg3", arg3.trim()));
        formparams.add(new BasicNameValuePair("arg4", arg4.trim()));
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
    public SimpleResponse MessagingApiService_getThread(long arg0) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/getThread";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
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
    public SimpleResponse BlogApiService_getBlogPostById(long arg0) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getBlogPostById";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
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
    public SimpleResponse UserApiService_createUser(String arg0, String arg1) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/createUser";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", arg0.trim()));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
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
    public SimpleResponse BlogApiService_deleteComment(long arg0) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/deleteComment";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
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
    public SimpleResponse BlogApiService_postReply(long arg0, String arg1) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/postReply";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
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
    public SimpleResponse MessagingApiService_replyMessage(long arg0, String arg1) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/replyMessage";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
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
    public SimpleResponse BlogApiService_deleteBlogPost(long arg0) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/deleteBlogPost";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
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
    public SimpleResponse MessagingApiService_getMessages(long arg0, long arg1, String arg2, String arg3) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/getMessages";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", String.valueOf(arg1)));
        formparams.add(new BasicNameValuePair("arg2", arg2.trim()));
        formparams.add(new BasicNameValuePair("arg3", arg3.trim()));
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
    public SimpleResponse UserApiService_loginUser(String arg0, String arg1) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/loginUser";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", arg0.trim()));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
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
    public SimpleResponse BlogApiService_getBlogComments(long arg0, int arg1) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getBlogComments";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", String.valueOf(arg1)));
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
    public SimpleResponse UserApiService_registerGcmKey(String arg0) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/registerGcmKey";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", arg0.trim()));
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
    public SimpleResponse BlogApiService_getBlogPosts(String arg0, int arg1, int arg2, String arg3, String arg4) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getBlogPosts";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", arg0.trim()));
        formparams.add(new BasicNameValuePair("arg1", String.valueOf(arg1)));
        formparams.add(new BasicNameValuePair("arg2", String.valueOf(arg2)));
        formparams.add(new BasicNameValuePair("arg3", arg3.trim()));
        formparams.add(new BasicNameValuePair("arg4", arg4.trim()));
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
    public SimpleResponse MessagingApiService_sendMessage(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws IOException, NotAuthorizedException
    {
        String path = "/MessagingApiService/sendMessage";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", arg0.trim()));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
        formparams.add(new BasicNameValuePair("arg2", arg2.trim()));
        formparams.add(new BasicNameValuePair("arg3", arg3.trim()));
        formparams.add(new BasicNameValuePair("arg4", arg4.trim()));
        formparams.add(new BasicNameValuePair("arg5", arg5.trim()));
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
    public SimpleResponse BlogApiService_getTopLevelBlogComments(long arg0, int arg1) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/getTopLevelBlogComments";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", String.valueOf(arg1)));
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
    public SimpleResponse BlogApiService_postComment(long arg0, String arg1) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/postComment";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
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
    public SimpleResponse UserApiService_AddTwo(int arg0, int arg1) throws IOException, NotAuthorizedException
    {
        String path = "/UserApiService/AddTwo";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", String.valueOf(arg1)));
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
    public SimpleResponse BlogApiService_updateBlogPost(long arg0, String arg1, String arg2, String arg3, String arg4) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/updateBlogPost";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
        formparams.add(new BasicNameValuePair("arg2", arg2.trim()));
        formparams.add(new BasicNameValuePair("arg3", arg3.trim()));
        formparams.add(new BasicNameValuePair("arg4", arg4.trim()));
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
    public SimpleResponse BlogApiService_updateComment(long arg0, String arg1) throws IOException, NotAuthorizedException
    {
        String path = "/BlogApiService/updateComment";
        HttpPost post = new HttpPost(domain  + path);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("arg0", String.valueOf(arg0)));
        formparams.add(new BasicNameValuePair("arg1", arg1.trim()));
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
