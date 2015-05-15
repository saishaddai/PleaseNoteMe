package com.nearsoft.pleasenoteme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.nearsoft.pleasenoteme.repository.NotesDbAdapter;

public class MainActivity extends Activity {

    public final static String EXTRA_ID_NOTE = "com.nearsoft.pleasenote.ID_NOTE";
    public final static String EXTRA_TITLE = "com.nearsoft.pleasenote.TITLE";
    public final static String EXTRA_CONTENT = "com.nearsoft.pleasenote.CONTENT";

    private NotesDbAdapter notesDbAdapter;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesDbAdapter = new NotesDbAdapter(this);
        notesDbAdapter.open();
        //TODO remove when needed. For demo purposes only
//        notesDbAdapter.deleteAllNotes();
//        notesDbAdapter.insertSomeNotes();

        displayNotes();

    }

    private void displayNotes() {
        Cursor cursor = notesDbAdapter.getAllNotes();
        String[] columns = new String[]{
                NotesDbAdapter.KEY_ROWID,
                NotesDbAdapter.KEY_TITLE,
                NotesDbAdapter.KEY_CONTENT
        };

        int[] to = new int[]{R.id.note_id, R.id.note_title};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.notes_info, cursor, columns, to, 0);
        ListView listView = (ListView) findViewById(R.id.note_list);
        listView.setAdapter(simpleCursorAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String noteId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));

                setupIntent(noteId, title, content, (Context) MainActivity.this);
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.note_filter);
        myFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                simpleCursorAdapter.getFilter().filter(s.toString());
            }
        });

        simpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return notesDbAdapter.getNotesByName(constraint.toString());
            }
        });
    }

    private void setupIntent(String noteId, String title, String content, Context context) {
        Intent intent = new Intent(context, EditNoteActivity.class);
        intent.putExtra(EXTRA_ID_NOTE, noteId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CONTENT, content);
        startActivity(intent);
    }

    public void addNewNote(View view) {
        setupIntent("", "", "", this);
    }

    public void useDictionary(View view) {
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }
}
