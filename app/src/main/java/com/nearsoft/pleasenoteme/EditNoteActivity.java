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

import com.nearsoft.pleasenoteme.entity.Note;
import com.nearsoft.pleasenoteme.repository.NotesRepository;
import com.nearsoft.pleasenoteme.utils.StringUtilities;


public class EditNoteActivity extends Activity {

    private static String BACK_BUTTON_VALUE = "Back";
    private static int MAX_TITLE_SIZE = 100;
    private static int MAX_CONTENT_SIZE = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        setupActivity();
    }

    public void cancelOrDeleteButtonPressedAction(View view) {
        if (isBackButtonPressed(view)) {
            showWarning(R.string.warning_no_changes_saved);
            finish();
        } else {
            showDeleteConfirmationDialog();
        }
    }

    public void saveButtonPressedAction(View view) {
        String noteId = getNoteId();
        EditText titleComponent = getEditTextById(R.id.edit_title);
        EditText contentComponent = getEditTextById(R.id.edit_content);

        Note note = getNoteFromComponents(titleComponent, contentComponent);
        if (note != null) {
            updateOrInsertNote(noteId, note);
            showWarning(R.string.warning_note_saved);
            finish();//TODO recharge the cursor of notes getting
        } else {
            showWarning(R.string.warning_note_validation_invalid);
        }
    }

    private void updateOrInsertNote(String noteId, Note note) {
        NotesRepository notesRepository = new NotesRepository(this);
        notesRepository.open();
        if (!noteId.isEmpty()) {
            notesRepository.updateNote(note.getId(), note.getTitle(), note.getContent());
        } else {
            notesRepository.createNote(note.getTitle(), note.getContent());
        }
    }

    private void setupActivity() {
        String noteId = getNoteId();
        boolean isEditingNote = !noteId.isEmpty();
        if (isEditingNote) {
            setStylesToEditionMode();
        }
    }

    private void setStylesToEditionMode() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.EXTRA_TITLE);
        String content = intent.getStringExtra(MainActivity.EXTRA_CONTENT);
        setupTextAndBackgroundEditText(title, R.id.edit_title);
        setupTextAndBackgroundEditText(content, R.id.edit_content);
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setText(R.string.button_delete_note);
    }

    private void setupTextAndBackgroundEditText(String message, int editId) {
        EditText editTextTitle = getEditTextById(editId);
        editTextTitle.setText(message);
        editTextTitle.getBackground().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
    }

    private String getNoteId() {
        return getIntent().getStringExtra(MainActivity.EXTRA_ID_NOTE);
    }

    private boolean isBackButtonPressed(View view) {
        Button deleteButton = (Button) view.findViewById(R.id.delete_button);
        String valueButton = deleteButton.getText().toString();
        return valueButton.equals(BACK_BUTTON_VALUE);
    }

    private void showDeleteConfirmationDialog() {
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

    private void deleteNoteAndBackToMainActivity(String noteId, Context context) {
        NotesRepository notesRepository = new NotesRepository(context);
        notesRepository.open();
        notesRepository.deleteNoteById(noteId);
        showWarning(R.string.warning_note_erased);
        finish();
    }

    private EditText getEditTextById(int edit_title) {
        return (EditText) findViewById(edit_title);
    }

    private Note getNoteFromComponents(EditText titleComponent, EditText contentComponent) {
        if (titleComponent == null || contentComponent == null) {
            return null;
        }
        String title = titleComponent.getText().toString();
        String content = contentComponent.getText().toString();

        boolean validatesNote = StringUtilities.isValidStringLength(title, MAX_TITLE_SIZE) &&
                StringUtilities.isValidStringLength(content, MAX_CONTENT_SIZE);

        if (validatesNote) {
            return new Note(title, content);
        }
        return null;
    }

    private void showWarning(int idMessage) {
        Toast.makeText(getApplicationContext(), getString(idMessage), Toast.LENGTH_SHORT).show();
    }
}
