package com.metalrain.simple.api.model;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Adam on 1/5/2015.
 */
public class BlogComment implements Serializable{
    public long id;
    public long parent_id;
    public String content;
    public User author;
    public Date dateCreated;
    public int score=0;
    public int depth=0;

    public Boolean visible = true;
    public Boolean client_visible = true;
    public List<BlogComment> children = new ArrayList();

    public BlogComment(JsonObject obj) {
        id = !obj.has("id")?0:obj.get("id").getAsLong();
        content = !obj.has("content")?"":obj.get("content").getAsString();
        author = !obj.has("author")?null:new User(obj.get("author").getAsJsonObject());
        score = !obj.has("score")?0:obj.get("score").getAsInt();
        visible = !obj.has("visible")?true:obj.get("visible").getAsBoolean();
        parent_id = obj.get("parent_id").getAsLong();
        dateCreated = new Date(obj.get("dateCreated").getAsString());
    }

    @Override
    public String toString() {
        return "BlogComment{" +
                "id=" + id +
                ", parent_id=" + parent_id +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", dateCreated=" + dateCreated +
                ", score=" + score +
                ", visible=" + visible +
                ", children=" + children +
                '}';
    }

    public void registerChild(BlogComment comment) {
        if (!children.contains(comment)) {
            children.add(comment);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogComment that = (BlogComment) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
