package com.metalrain.simple.api;

import com.metalrain.simple.api.model.BlogPost;
import com.metalrain.simple.api.model.Category;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Adam on 2/18/2015.
 */
public class BlogPostManager {

    private final Subject<Object, Object> _bus = new SerializedSubject(PublishSubject.create());
    public Observable<Object> toObserverable() {
        return _bus;
    }
    public void remove(BlogPost post) {
        posts.remove(post);
    }

    private final Collection<BlogPost> posts = new HashSet<BlogPost>();
    private final Collection<Category> usedCategories = new HashSet<Category>();
    private final Map<Long, BlogPost> id_lookup = new HashMap<Long, BlogPost>();

    public void addBlogPost(BlogPost blogPost){
        if (!posts.contains(blogPost))posts.add(blogPost);
        for (Category c:blogPost.categories) {
            if (!usedCategories.contains(c)) usedCategories.add(c);
        }
        id_lookup.put(blogPost.id, blogPost);
    }

    public BlogPost getBlogPostById(long id) {
        return id_lookup.get(id);
    }

    public List<BlogPost> getBlogPostsForCategory(final Category c) {
        ArrayList<BlogPost> results = new ArrayList(posts);

        CollectionUtils.filter(results, new Predicate<BlogPost>() {
            @Override
            public boolean evaluate(BlogPost object) {
                return object.categories.contains(c);
            }
        });
        return results;

    }

    public boolean hasPreloadedCategory(Category category) {
        return usedCategories.contains(category);
    }
}
