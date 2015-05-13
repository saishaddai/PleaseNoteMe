package com.nearsoft.pleasenoteme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nearsoft.pleasenoteme.bean.Note;
import com.nearsoft.pleasenoteme.repository.NotesDbAdapter;


public class EditNoteActivity extends Activity {

    private static String BACK_BUTTON_VALUE = "Back";
    private static int MAX_TITLE_SIZE = 100;
    private static int MAX_CONTENT_SIZE = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.EXTRA_ID_NOTE);
        String title = intent.getStringExtra(MainActivity.EXTRA_TITLE);
        String content = intent.getStringExtra(MainActivity.EXTRA_CONTENT);

        if(!noteId.isEmpty()) { //is editing the note
            EditText editTextTitle = (EditText) findViewById(R.id.edit_title);
            editTextTitle.setText(title);
            editTextTitle.getBackground().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

            EditText editTextContent = (EditText) findViewById(R.id.edit_content);
            editTextContent.setText(content);
            editTextContent.getBackground().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

            Button deleteButton = (Button) findViewById(R.id.delete_button);
            deleteButton.setText(R.string.button_delete_note);

            Toast.makeText(getApplicationContext(),
                    noteId, Toast.LENGTH_LONG).show();
        }
    }

    public void deleteMessage(View view) {
        Button deleteButton = (Button) view.findViewById(R.id.delete_button);
        String valueButton = deleteButton.getText().toString();
        if(valueButton.equals(BACK_BUTTON_VALUE)) {
            Toast.makeText(getApplicationContext(),
                    R.string.warning_no_changes_saved, Toast.LENGTH_SHORT).show();
            EditNoteActivity.this.finish();
        } else {
            //this means the delete button was pressed
            new AlertDialog.Builder(this)
                    .setMessage(R.string.warning_delete_this_note)
                    .setCancelable(false)
                    .setPositiveButton(R.string.warning_confirm_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = getIntent();
                            String noteId = intent.getStringExtra(MainActivity.EXTRA_ID_NOTE);
                            deleteNoteAndBackToMainActivity(noteId, EditNoteActivity.this);
                        }
                    })
                    .setNegativeButton(R.string.warning_cancel_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void deleteNoteAndBackToMainActivity(String noteId, Context context) {
        NotesDbAdapter dbHelper = new NotesDbAdapter(context);
        dbHelper.open();
        dbHelper.deleteNoteById(noteId);

        Toast.makeText(getApplicationContext(),
                R.string.warning_note_erased, Toast.LENGTH_SHORT).show();

        EditNoteActivity.this.finish();
    }

    public void saveMessage(View view) {
        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.EXTRA_ID_NOTE);
        EditText titleComponent = (EditText) findViewById(R.id.edit_title);
        EditText contentComponent = (EditText) findViewById(R.id.edit_content);

        Note note = getNoteFromComponents(titleComponent, contentComponent);
        if( note != null ) {
            NotesDbAdapter dbHelper = new NotesDbAdapter(this);
            dbHelper.open();
            if(!noteId.isEmpty()) { //is editing the note
                dbHelper.updateNote(note.getId(), note.getTitle(), note.getContent());
            } else {
                dbHelper.createNote(note.getTitle(), note.getContent());
            }
            Toast.makeText(getApplicationContext(),
                    R.string.warning_note_saved, Toast.LENGTH_SHORT).show();
            EditNoteActivity.this.finish();
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.warning_note_validation_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    private Note getNoteFromComponents(EditText titleComponent, EditText contentComponent) {
        if(titleComponent == null || contentComponent == null) {
            return null;
        }
        String title = titleComponent.getText().toString();
        String content = contentComponent.getText().toString();

        if (!title.isEmpty() && title.length() <= MAX_TITLE_SIZE &&
                !content.isEmpty() && content.length() <= MAX_CONTENT_SIZE) {
            return new Note(title, content);
        }
        return null;
    }
}
