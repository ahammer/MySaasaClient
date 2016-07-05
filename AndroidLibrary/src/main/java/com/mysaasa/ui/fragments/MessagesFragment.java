package com.mysaasa.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.ui.ActivityChat;
import com.mysaasa.ui.views.StandardMessageView;
import com.mysaasa.R;
import com.mysaasa.api.model.Message;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Adam on 4/3/2016.
 */
public class MessagesFragment extends Fragment {
        private ListView list;

        public MessagesFragment() {}

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.activity_messages, null);
            list = (ListView) vg.findViewById(R.id.list);
            return vg;
        }

        @Override
        public void onStart() {
            super.onStart();
            MySaasaApplication.getService().getMessagesManager().getMessages()
            .observeOn(AndroidSchedulers.mainThread())
                    .toSortedList((message, message2) -> message2.timeSent.compareTo(message.timeSent))
                    .subscribe(
            list-> {
                MessagesFragment.this.list.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return list.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return list.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return list.get(position).id;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        StandardMessageView messageView = new StandardMessageView(getActivity());
                        messageView.setMessage(list.get(position));
                        return messageView;
                    }
                });
            },
            e->{
                Crouton.makeText(getActivity(), "Error loading messages: " + e.getMessage(), Style.ALERT).show();
            });

            list.setOnItemClickListener((parent, view, position, id) -> {
                Message message = (Message) parent.getAdapter().getItem(position);
                ActivityChat.StartChat(getActivity(), message);
            });
        }
}
