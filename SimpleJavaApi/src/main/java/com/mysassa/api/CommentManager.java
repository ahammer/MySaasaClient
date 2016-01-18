package com.mysassa.api;

import com.mysassa.api.model.BlogComment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Adam on 1/6/2015.
 */
public class CommentManager {
    private final Service service;
    public Map<Long, List<BlogComment>> comments = new HashMap<Long, List<BlogComment>>();
    public Map<Long, List<BlogComment>> toplevel_comments = new HashMap<Long, List<BlogComment>>();
    public Map<Long, BlogComment> id_lookup = new HashMap<Long, BlogComment>();

    public CommentManager(Service service) {
        this.service = service;
     //   service.bus.register(this);
    }

    public List<BlogComment> getComments(long blogpost_id) {
        List<BlogComment> result = toplevel_comments.get(blogpost_id);
        if (result != null) return processTopLevelComments(result);
        service.retrieveBlogComments(blogpost_id,1000);
        return Collections.EMPTY_LIST;
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
