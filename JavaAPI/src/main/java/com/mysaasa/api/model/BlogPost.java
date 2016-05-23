package com.mysaasa.api.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A BlogPost is just a Article or stream of "Text articles" with a series of linked images.
 * <p/>
 * They can be used to represent articles or news, depending on how you choose to categorize them.
 * <p/>
 * They will be associated with a organization and visible to websites and apps in that organization.
 */
public class BlogPost implements Serializable {
    private static final long serialVersionUID = 1L;
    public final long id;
    public final String title;
    public final String body;
    public final String summary;
    public final String subtitle;


    public User author;
    private Date dateCreated;
    private Date datePublished;
    private Boolean published;
    public List<com.mysaasa.api.model.Category> categories = new ArrayList<com.mysaasa.api.model.Category>();



    public BlogPost(JsonObject post) {
        id = post.get("id").getAsLong();
        title = post.get("title").getAsString();
        body = post.has("body")?post.get("body").getAsString():null;
        subtitle = post.has("subtitle")?post.get("subtitle").getAsString():null;
        summary = post.has("summary")?post.get("summary").getAsString():null;
        author = new User(post.get("author").getAsJsonObject());
        JsonArray cats = post.getAsJsonArray("categories");
        for (int i=0;i<cats.size();i++) {
            categories.add(new com.mysaasa.api.model.Category(cats.get(i).getAsJsonObject()));
        }


    }

    public BlogPost(BlogPost post, String title, String subtitle, String summary, String body) {
        this.id = post.id;
        this.author = post.author;
        dateCreated = post.dateCreated;
        datePublished = post.datePublished;
        published = post.published;
        this.title = title;
        this.body = body;
        this.summary = summary;
        this.subtitle = subtitle;
    }


    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", author=" + author +
                ", datePublished=" + datePublished +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", body='" + body + '\'' +
                ", published=" + published +
                ", categories=" + categories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogPost post = (BlogPost) o;

        if (id != post.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}