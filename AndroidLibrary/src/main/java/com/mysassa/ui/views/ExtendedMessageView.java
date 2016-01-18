package com.mysassa.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysassa.R;
import com.mysassa.api.model.Message;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by adam on 15-02-22.
 */
public class ExtendedMessageView extends FrameLayout {
    private final TextView body;
    private Message message;

    private final TextView title, author,date;
    public ExtendedMessageView(Context context) {
        super(context);
        inflate(context, R.layout.listitem_message_full, this);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        date = (TextView) findViewById(R.id.date);
        body = (TextView) findViewById(R.id.body);
    }

    public void setMessage(Message message) {
        this.message = message;
        title.setText(message.title);
        if (message.sender != null) {
            author.setText(message.sender.identifier);
        } else {
            author.setText(message.senderContactInfo.toString());
        }

        date.setText(DateFormat.getDateInstance(DateFormat.SHORT, Locale.CANADA).format(message.timeSent));
        body.setText(message.body);

        if (message.messageThreadRoot != null) {
            title.setVisibility(View.GONE);
        }
    }

    public Message getMessage() {
        return message;
    }
}
