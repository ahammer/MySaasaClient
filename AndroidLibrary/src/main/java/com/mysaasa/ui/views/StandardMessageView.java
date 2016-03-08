package com.mysaasa.ui.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mysassa.R;
import com.mysassa.api.model.Message;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by adam on 15-02-22.
 */
public class StandardMessageView extends FrameLayout {
    private Message message;

    private final TextView title, author,date;
    public StandardMessageView(Context context) {
        super(context);
        inflate(context, R.layout.listitem_message, this);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        date = (TextView) findViewById(R.id.date);
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
    }

    public Message getMessage() {
        return message;
    }
}
