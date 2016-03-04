package com.mysassa.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mysassa.ApplicationSectionsManager;
import com.mysassa.R;
import com.mysassa.SimpleApplication;
import com.mysassa.api.model.BlogPost;
import com.mysassa.api.model.Category;

import java.io.Serializable;

/**
 * Created by administrator on 2014-06-30.
 */
public class ActivityPostToBlog extends Activity {
    public static final int REQUEST_CODE = 200202;

    static class State implements Serializable{
        Category category;
        ApplicationSectionsManager.CategoryDef categoryDef;
        BlogPost post;
        boolean editMode = false;
    }



    State state = new State();
    EditText title;
    EditText postTitle;
    EditText subtitle;
    EditText summary;
    EditText body;
    Button postButton;
    private TextView categoryTitle;




    public static void postComment(Activity ctx, Category c) {
        Intent i = new Intent(ctx, ActivityPostToBlog.class);
        i.putExtra("category",c);
        ctx.startActivityForResult(i, REQUEST_CODE);
    }
    public static void editComment(Activity ctx, BlogPost post) {
        Intent i = new Intent(ctx, ActivityPostToBlog.class);
        i.putExtra("post",post);
        ctx.startActivityForResult(i, REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("state")) {
            state = (State) savedInstanceState.getSerializable("state");
        } else {
            state.category = (Category) getIntent().getSerializableExtra("category");
            if (state.category != null) {
                state.categoryDef = SimpleApplication.getInstance().getAndroidCategoryManager().getCategoryDef(state.category);
            }
            state.post = (BlogPost) getIntent().getSerializableExtra("post");
            state.editMode = state.post!=null;
        }

        setContentView(R.layout.activity_post_to_blog);
        categoryTitle = (TextView) findViewById(R.id.category);
        title = (EditText) findViewById(R.id.title);
        subtitle= (EditText) findViewById(R.id.subtitle);
        summary = (EditText) findViewById(R.id.summary);
        body = (EditText) findViewById(R.id.body);
        postButton = (Button) findViewById(R.id.post);
        if (state.editMode) {
            postButton.setText("Save post");
        }
        if (state.categoryDef!=null) categoryTitle.setText(state.categoryDef.title);
        else categoryTitle.setVisibility(View.GONE);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state.editMode) {
                    BlogPost updatedPost = new BlogPost(
                            state.post,
                            title.getText().toString(),
                            subtitle.getText().toString(),
                            summary.getText().toString(),
                            body.getText().toString());
                            Intent i = new Intent();

                    SimpleApplication.getService().updateBlogPost(
                            state.post,
                            title.getText().toString(),
                            subtitle.getText().toString(),
                            summary.getText().toString(),
                            body.getText().toString()

                    );
                    i.putExtra("post",updatedPost);
                    setResult(Activity.RESULT_OK,i);
                    finish();
                    return;

                } else {
                    SimpleApplication.getService().postToBlog(
                            title.getText().toString(),
                            subtitle.getText().toString(),
                            summary.getText().toString(),
                            body.getText().toString(),
                            state.category.name
                    );
                }
                setResult(Activity.RESULT_OK);
                finish();


            }
        });


        if (!SimpleApplication.getService().getState().authenticated) {
            Intent i = new Intent(this, ActivitySignin.class);
            startActivityForResult(i, 10010);
        }

        if (state.editMode && savedInstanceState == null) {
            title.setText(state.post.title);
            subtitle.setText(state.post.subtitle);
            body.setText(state.post.body);
            summary.setText(state.post.summary);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state","state");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010 && resultCode != Activity.RESULT_OK) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

}
