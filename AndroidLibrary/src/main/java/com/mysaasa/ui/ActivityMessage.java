package com.mysaasa.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.views.ExtendedMessageView;
import com.mysassa.R;
import com.mysassa.api.model.Message;

import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 2/16/2015.
 */
public class ActivityMessage extends Fragment {
    ListView threadList;
    private Message rootMessage;
    private List<Message> thread;
    private Button replyButton;
    private EditText replyText;

    public ActivityMessage() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_blog_comments, container);
    }


}
