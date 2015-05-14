package com.nearsoft.pleasenoteme;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DictionaryActivityTest extends ActivityInstrumentationTestCase2<DictionaryActivity> {
    private DictionaryActivity dictionaryActivity;
    private EditText searchEditText;
    private Button searchButton;
    private TextView wordTextView;
    private TextView meaningTextView;

    public DictionaryActivityTest() {
        super(DictionaryActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dictionaryActivity = getActivity();
        searchEditText = (EditText) dictionaryActivity.findViewById(R.id.search_word);
        searchButton = (Button) dictionaryActivity.findViewById(R.id.search_button);
        wordTextView = (TextView) dictionaryActivity.findViewById(R.id.definition_word);
        meaningTextView = (TextView) dictionaryActivity.findViewById(R.id.definition_meanings);
    }

    public void testPreconditions() {

        //TODO validate the correct view components appear in this activity
        assertNotNull("DictionaryActivity is available", dictionaryActivity);
        assertNotNull("search edit field is available", searchEditText);
        assertNotNull("search button is available", searchButton);
        assertNotNull("word text view is available", wordTextView);
        assertNotNull("meaning text view is available", meaningTextView);
    }

    public void testMainActivity_checkLabelsAdnHints() {
        //TODO validate the correct labels and hints are set in this activity. Compare with R.string.text_name

    }
}