package com.metalrain.simple.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.common.eventbus.Subscribe;
import com.metalrain.simple.R;
import com.metalrain.simple.SimpleApplication;
import com.metalrain.simple.api.DeletedBlogPost;
import com.metalrain.simple.api.messages.BlogCommentsRetrievedMessage;
import com.metalrain.simple.api.messages.DeletedBlogComment;
import com.metalrain.simple.api.model.BlogComment;
import com.metalrain.simple.api.model.BlogPost;
import com.metalrain.simple.ui.ActivityPostComment;
import com.metalrain.simple.ui.views.BlogCommentView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Adam on 1/5/2015.
 */
public class BlogCommentViewer extends Fragment {
    BlogPost post;
    ListView comments;
    long selected_comment_id=0;

    private Action1<Object> messageHook;

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
        SimpleApplication.getService().bus.toObserverable().subscribe(messageHook = new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof DeletedBlogPost) {
                    blogPostDeleted((DeletedBlogComment) o);
                } else if (o instanceof  BlogCommentsRetrievedMessage) {
                    blogCommentsReceived((BlogCommentsRetrievedMessage) o);
                }

            }
        });
        blogCommentsReceived(null);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public BlogPost getPost() {
        return post;
    }

    public void setPost(BlogPost post) {
        if (post == null) return;
        this.post = post;
        blogCommentsReceived(null);
    }


    public void blogPostDeleted(DeletedBlogComment message) {
        blogCommentsReceived(null);
    }

    public void blogCommentsReceived(BlogCommentsRetrievedMessage message) {
        if (post == null) return;
        final List<BlogComment> list = SimpleApplication.getService().getBlogComments(post.id, 100, false);
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
                    comments.setAdapter(new BaseAdapter() {
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
                    });

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

    public void refresh() {
        SimpleApplication.getService().getBlogComments(post.id, 100,true);
    }
}
