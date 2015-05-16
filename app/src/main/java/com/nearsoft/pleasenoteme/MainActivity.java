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

import com.nearsoft.pleasenoteme.repository.NotesRepository;

public class MainActivity extends Activity {

    public final static String EXTRA_ID_NOTE = "com.nearsoft.pleasenote.ID_NOTE";
    public final static String EXTRA_TITLE = "com.nearsoft.pleasenote.TITLE";
    public final static String EXTRA_CONTENT = "com.nearsoft.pleasenote.CONTENT";

    private NotesRepository notesRepository;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivity();
    }

    private void setupActivity() {
        notesRepository = new NotesRepository(this);
        notesRepository.open();
        displayNotes();
    }

    private void displayNotes() {
        getNotesCursor();
        setNotesInView();
        addNotesFilter();
    }

    private void addNotesFilter() {
        EditText myFilter = (EditText) findViewById(R.id.note_filter);
        myFilter.addTextChangedListener(getFilterTextWatcher());
        simpleCursorAdapter.setFilterQueryProvider(getFilterQueryProvider());
    }

    private FilterQueryProvider getFilterQueryProvider() {
        return new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return notesRepository.getNotesByName(constraint.toString());
            }
        };
    }

    private TextWatcher getFilterTextWatcher() {
        return new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                simpleCursorAdapter.getFilter().filter(s.toString());
            }
        };
    }

    private void setNotesInView() {
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

                setupIntent(noteId, title, content, MainActivity.this);
            }
        });
    }

    private void getNotesCursor() {
        Cursor cursor = notesRepository.getAllNotes();
        String[] columns = new String[]{
                NotesRepository.KEY_ROWID,
                NotesRepository.KEY_TITLE,
                NotesRepository.KEY_CONTENT
        };
        int[] to = new int[]{R.id.note_id, R.id.note_title};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.notes_info, cursor, columns, to, 0);
    }

    private void setupIntent(String noteId, String title, String content, Context context) {
        Intent intent = new Intent(context, EditNoteActivity.class);
        intent.putExtra(EXTRA_ID_NOTE, noteId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CONTENT, content);
        startActivity(intent);
    }

    public void noteMeButtonPressedAction(View view) {
        setupIntent("", "", "", this);
    }

    public void dictionaryButtonPressedAction(View view) {
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }
}
