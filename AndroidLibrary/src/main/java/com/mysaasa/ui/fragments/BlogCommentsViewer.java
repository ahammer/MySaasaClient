package com.mysaasa.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.ActivityPostComment;
import com.mysaasa.ui.adapters.BlogCommentsAdapter;
import com.mysassa.R;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Adam on 1/5/2015.
 */
public class BlogCommentsViewer extends Fragment {
    BlogPost post;
    ListView comments;
    long selectedCommentId =0;
    private Subscription subscription;


    public void setselectedCommentId(long selected_comment_id) {
        this.selectedCommentId = selected_comment_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_blogcommentviewer,container);
        comments = (ListView) v.findViewById(R.id.comments);
        comments.setDividerHeight(1);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        MySaasaApplication.getService().bus.register(this);
        //setBlogComments(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        MySaasaApplication.getService().bus.unregister(this);
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    public BlogPost getPost() {
        return post;
    }

    public void setPost(BlogPost post) {
        if (post == null) return;
        this.post = post;

        subscription = MySaasaApplication
                .getService()
                .getCommentManager()
                .getBlogCommentsObservable(post)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(this::setBlogComments,this::handleException);
    }

    private void handleException(Throwable t) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
        });
    }
    private void setBlogComments(final List<BlogComment> list) {
        if (post == null) return;
        if (getActivity() == null) return;
        if (list.size() == 0) {
            setupEmptyList();
        } else {
            setupCommentAdapter(list);
        }
    }

    private void setupCommentAdapter(List<BlogComment> list) {
        comments.setAdapter(new BlogCommentsAdapter(list, selectedCommentId, post));
        findCurrentSelection();
    }

    private void findCurrentSelection() {
        if (selectedCommentId != 0) {
            for (int i=0;i<comments.getAdapter().getCount();i++) {
                if (comments.getAdapter().getItem(i) instanceof BlogComment) {
                    if (((BlogComment) comments.getAdapter().getItem(i)).getId() == selectedCommentId) {
                        comments.setSelection(i);
                        break;
                    }
                }
            };
        }
    }

    private void setupEmptyList() {
        comments.setAdapter(new EmptyListAdapter() {
            @Override
            protected void Clicked() {
                ActivityPostComment.postComment(getActivity(), post, null);
            }

            @Override
            protected String getText() {
                return "--- No Comments ---";
            }
        });
    }

}
