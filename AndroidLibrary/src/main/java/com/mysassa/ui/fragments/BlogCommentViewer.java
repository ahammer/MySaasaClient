package com.mysassa.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.model.BlogComment;
import com.mysassa.api.model.BlogPost;
import com.mysassa.ui.ActivityPostComment;
import com.mysassa.ui.views.BlogCommentView;

import java.util.Collections;
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
        MySaasaAndroidApplication.getService().bus.register(this);
        //blogCommentsReceived(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        MySaasaAndroidApplication.getService().bus.unregister(this);
    }

    public BlogPost getPost() {
        return post;
    }

    public void setPost(BlogPost post) {
        if (post == null) return;
        this.post = post;
        //blogCommentsReceived(null);
    }



    /*
    public void blogCommentsReceived(BlogCommentsRetrievedMessage message) {
        if (post == null) return;

        final List<BlogComment> list = Collections.EMPTY_LIST;
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
*/
}
