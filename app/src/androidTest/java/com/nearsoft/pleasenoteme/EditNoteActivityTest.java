package com.nearsoft.pleasenoteme;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EditNoteActivityTest extends ActivityInstrumentationTestCase2<EditNoteActivity> {
    private EditNoteActivity editNoteActivity;
    private Button saveButton;
    private Button deleteButton;
    private EditText editTitle;
    private EditText editContent;

    public EditNoteActivityTest() {
        super(EditNoteActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        editNoteActivity = getActivity();
        saveButton = (Button) editNoteActivity.findViewById(R.id.button);
        deleteButton = (Button) editNoteActivity.findViewById(R.id.delete_button);
        editTitle = (EditText) editNoteActivity.findViewById(R.id.edit_title);
        editContent = (EditText) editNoteActivity.findViewById(R.id.edit_content);
    }

    public void testPreconditions() {
        assertNotNull("EditNoteActivity is available", editNoteActivity);
        assertNotNull("Save button is available", saveButton);
        assertNotNull("Delete button is available", deleteButton);
        assertNotNull("edit title is available", editTitle);
        assertNotNull("edit content is available", editContent);
    }

    public void testMainActivity_checkLabelsAdnHints() {

    }
}