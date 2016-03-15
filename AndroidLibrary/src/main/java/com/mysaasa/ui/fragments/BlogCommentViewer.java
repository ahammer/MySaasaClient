package com.mysaasa.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mysaasa.MySaasaAndroidApplication;
import com.mysaasa.ui.ActivityPostComment;
import com.mysaasa.ui.views.BlogCommentView;
import com.mysassa.R;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Adam on 1/5/2015.
 */
public class BlogCommentViewer extends Fragment {
    BlogPost post;
    ListView comments;
    long selected_comment_id=0;
    private Subscription subscription;


    public void setSelected_comment_id(long selected_comment_id) {
        this.selected_comment_id = selected_comment_id;
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
        MySaasaAndroidApplication.getService().bus.register(this);
        //blogCommentsReceived(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        MySaasaAndroidApplication.getService().bus.unregister(this);
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

        subscription = MySaasaAndroidApplication
                .getService()
                .getBlogManager()
                .getBlogCommentsObservable(post)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toList().subscribe(new Action1<List<BlogComment>>() {
                    @Override
                    public void call(List<BlogComment> blogComments) {
                        blogCommentsReceived(blogComments);
                    }
                });
    }




    public void blogCommentsReceived(final List<BlogComment> list) {
        if (post == null) return;
        if (getActivity() == null) return;
        if (list.size() == 0) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    comments.setAdapter(new EmptyListAdapter() {
                        @Override
                        protected void Clicked() {
                            ActivityPostComment.postComment(getActivity(), post,null);
                        }

                        @Override
                        protected String getText() {
                            return "--- No Comments ---";
                        }
                    });
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    comments.setAdapter(new MyBlogCommentsAdapter(list));

                    if (selected_comment_id != 0) {
                        for (int i=0;i<list.size();i++) {
                            if (list.get(i).id == selected_comment_id) {
                                comments.setSelection(i);
                                break;

                            }
                        };
                    }


                }
            });
        }

    }

    private class MyBlogCommentsAdapter extends BaseAdapter {
        private final List<BlogComment> list;

        public MyBlogCommentsAdapter(List<BlogComment> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return list.get(i).id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            BlogCommentView bcv = new BlogCommentView(getActivity()) {
                @Override
                protected BlogPost getBlogPost() {
                    return post;
                }

                @Override
                protected void notifyChildVisibilityChanged() {
                    blogCommentsReceived(null);
                }
            };

            bcv.setComment(list.get(i));
            if (list.get(i).id == selected_comment_id) {
                bcv.setHighlight(true);
            } else {
                bcv.setHighlight(false);
            }

            return bcv;
        }
    }
}
