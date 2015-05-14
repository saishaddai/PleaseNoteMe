package com.nearsoft.pleasenoteme;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Button newNoteButton;
    private Button dictionaryButton;
    private EditText filterEditText;
    private ListView listOfNotes;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        newNoteButton = (Button) mainActivity.findViewById(R.id.note_me_button_new);
        dictionaryButton = (Button) mainActivity.findViewById(R.id.note_me_dictionary_button);
        filterEditText = (EditText) mainActivity.findViewById(R.id.note_filter);
        listOfNotes = (ListView) mainActivity.findViewById(R.id.note_list);
    }

    public void testPreconditions() {
        assertNotNull("MainActivity is available", mainActivity);
        assertNotNull("New note button is available", newNoteButton);
        assertNotNull("Dictionary Button is available", dictionaryButton);
        assertNotNull("Filter is available", filterEditText);
        assertNotNull("List of notes is available", listOfNotes);
    }

    public void testMainActivity_checkLabelsAdnHints() {

    }
}