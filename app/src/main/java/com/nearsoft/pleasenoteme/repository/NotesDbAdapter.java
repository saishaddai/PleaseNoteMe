package com.nearsoft.pleasenoteme.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    private static final String TAG = "noteMe";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "please_note_me";
    private static final String SQLITE_TABLE = "notes";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXIST " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY AUTOINCREMENT," +
                    KEY_TITLE + "," +
                    KEY_CONTENT + ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public long createNote(String title, String content) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_CONTENT, content);
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllNotes() {
        int doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public boolean deleteNoteById(String id) {
        int doneDelete = mDb.delete(SQLITE_TABLE, KEY_ROWID + "=" + id, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor getNotesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor;
        if (inputText.isEmpty())  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_TITLE, KEY_CONTENT},
                    null, null, null, null, null);
        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_TITLE, KEY_CONTENT},
                    KEY_TITLE + " LIKE '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAllNotes() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_CONTENT}, null, null, null, null, KEY_TITLE);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeNotes() {
        createNote("Yeah!","Please Note me");
        createNote("I wonder...","is what i though but not today");
        createNote("There is some place","where I can go");
        createNote("Hello","That's was fun but not that fun");
        createNote("He he","My gf told me I am a saint");
        createNote("Just like marty mcfly","Bringing back to the future");
        createNote("So What do you think?","I'll start selling chicken");
    }

}
