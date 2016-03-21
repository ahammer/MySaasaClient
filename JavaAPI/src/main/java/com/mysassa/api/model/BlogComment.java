package com.mysassa.api.model;

import com.google.gson.JsonObject;
import com.mysassa.api.CommentManager;
import com.mysassa.api.MySaasaClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Adam on 1/5/2015.
 */
public class BlogComment implements Serializable{
    private long id;
    private long parent_id;
    private String content;
    private User author;
    private Date dateCreated;
    private int score=0;

    private Boolean visible = true;
    public Boolean client_visible = true;

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
                '}';
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

    public long getId() {
        return id;
    }

    public long getParent_id() {
        return parent_id;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public int getScore() {
        return score;
    }

    public Boolean getVisible() {
        return visible;
    }

    public BlogComment getParent(CommentManager commentManager) {
        if (parent_id == 0) return null;
        BlogComment parent = commentManager.lookupCommentById(parent_id);
        return parent;
    }

    public int calculateDepth(CommentManager commentManager) {
        int depth = 0;
        BlogComment parent = getParent(commentManager);
        while (parent != null) {
            depth++;
            parent = parent.getParent(commentManager);
        }
        return depth;
    }

    public List<BlogComment> getChildren(CommentManager manager) {
        return manager.getChildren(this);
    }
}
