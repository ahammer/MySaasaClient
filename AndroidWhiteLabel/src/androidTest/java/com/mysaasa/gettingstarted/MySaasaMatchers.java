package com.mysaasa.gettingstarted;

import android.support.test.espresso.matcher.BoundedMatcher;

import com.mysaasa.api.CommentManager;
import com.mysaasa.api.model.BlogComment;

import org.hamcrest.Description;

/**
 * Created by Adam on 3/27/2016.
 */
public class MySaasaMatchers {
    public static BoundedMatcher<Object, BlogComment> withComment(final CommentManager commentManager, final String comment) {
        return new BoundedMatcher<Object, BlogComment>(BlogComment.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has comment title "+ comment);
            }

            @Override
            protected boolean matchesSafely(BlogComment item) {
                int sum = treeSum(item);

                return sum > 0;
            }

            private int treeSum(BlogComment item) {
                int sum = 0;
                for (BlogComment child:item.getChildren(commentManager)) {
                    sum += treeSum(child);
                }

                if (item.getContent().equalsIgnoreCase(comment))
                    return sum+1;
                return sum;
            }
        };
    }
}
