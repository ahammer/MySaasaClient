package com.mysaasa.ui.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.mysassa.api.model.Message;
import com.mysassa.api.model.User;

import java.util.List;

/**
 * Created by Adam on 4/11/2016.
 */
public class MessagesDatabase implements MySaasaMessageStorage {
    private MessageDatabaseInternal database;

    public MessagesDatabase() {}

    public void start(Context ctx) {
        if (database != null) throw new IllegalStateException("Database already initialized");
        database = new MessageDatabaseInternal(ctx);
    }

    public void close() {
        database.close();
    }
    @Override
    public List<Message> getRootMessage(User user) {
        return null;
    }

    @Override
    public void storeMessage(Message m) {
        checkDatabase();
        SQLiteDatabase db = database.getWritableDatabase();
        //TODO, do a query, check for an update, load the Message if it's a update
        Message fromDatabase = getMessageById(m.id);
        //db.insert(MessageEntry.TABLE_NAME, null, MessageEntry.messageToContentValues(m));

    }

    @Override
    public Message getMessageById(long id) {
        //TODO Db query for this ID and cast to message
        return null;
    }

    private void checkDatabase() {
        if (database == null) throw new IllegalStateException("Database not connected");
    }

    public static abstract class MessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "Messages";
        public static final String COLUMN_NAME_MESSAGE_ID = "id";
        public static final String COLUMN_NAME_RECIPIENT_ID = "recipient_user_id";
        public static final String COLUMN_NAME_SENDER_ID = "sender_user_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_TIMESENT = "timesent";
        public static final String COLUMN_NAME_READ = "read";
        public static final String COLUMN_NAME_SENDER_CONTACT_INFO_ID = "contact_info_id";

        public static ContentValues messageToContentValues(Message message) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_MESSAGE_ID, message.id);
            values.put(COLUMN_NAME_RECIPIENT_ID, message.id);
            values.put(COLUMN_NAME_SENDER_ID, message.id);
            values.put(COLUMN_NAME_TITLE, message.id);
            values.put(COLUMN_NAME_BODY, message.id);
            values.put(COLUMN_NAME_DATA, message.id);
            values.put(COLUMN_NAME_TIMESENT, message.id);
            values.put(COLUMN_NAME_READ, message.id);
            values.put(COLUMN_NAME_SENDER_CONTACT_INFO_ID, message.id);

            return values;
        }

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MESSAGE_ID              + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_RECIPIENT_ID            + " INTEGER, "+
                        COLUMN_NAME_SENDER_ID               + " INTEGER, "+
                        COLUMN_NAME_TITLE                   + " TEXT, "+
                        COLUMN_NAME_BODY                    + " TEXT, "+
                        COLUMN_NAME_DATA                    + " TEXT, "+
                        COLUMN_NAME_TIMESENT                + " TEXT, "+
                        COLUMN_NAME_READ                    + " INTEGER, "+
                        COLUMN_NAME_SENDER_CONTACT_INFO_ID  + " INTEGER)";

        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static final class MessageDatabaseInternal extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Messages";


        public MessageDatabaseInternal(Context context) {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(MessageEntry.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(MessageEntry.SQL_DROP_TABLE);
            onCreate(db);
        }
    }


}
