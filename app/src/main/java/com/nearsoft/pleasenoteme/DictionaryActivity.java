package com.nearsoft.pleasenoteme;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nearsoft.pleasenoteme.entity.Dictionary;
import com.nearsoft.pleasenoteme.utils.StringUtilities;
import com.nearsoft.pleasenoteme.service.DictionaryWebService;


public class DictionaryActivity extends Activity {

    DictionaryWebService dictionaryWebService;
    int MAX_KEYWORD_SIZE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        dictionaryWebService = new DictionaryWebService();
    }

    public void searchButtonPressedAction(View view) {
        String keyword = getAndSanitizeKeyword();
        if(StringUtilities.isValidStringLength(keyword, MAX_KEYWORD_SIZE)) {
            searchAndUpdateViews(keyword, R.id.definition_word, R.id.definition_meanings);
        } else {
            showWarning(R.string.warning_keyword_validation_failed);
        }


    }

    private void searchAndUpdateViews(String keyword, int idWordTextView, int idMeaningsTextView) {
        try {
            TextView wordTextView = cleanAndGetView(idWordTextView);
            TextView meaningsTextView = cleanAndGetView(idMeaningsTextView);
            Dictionary dictionary = dictionaryWebService.search(keyword, getString(R.string.warning_no_definition_found));
            wordTextView.setText(dictionary.getWord());
            meaningsTextView.setText(dictionary.getMeanings());
        } catch (Exception e) {
            Log.e(MainActivity.class.getName(), e.getMessage());
            showWarning(R.string.error_unreachable_ws);
            finish();
        }
    }

    private TextView cleanAndGetView(int viewId) {
        TextView wordTextView = (TextView) findViewById(viewId);
        wordTextView.setText(" ");
        return wordTextView;
    }

    private String getAndSanitizeKeyword() {
        EditText editText = (EditText) findViewById(R.id.search_word);
        String keyword = editText.getText().toString();
        keyword = StringUtilities.sanitizeKeyWord(keyword);
        return keyword;
    }

    private void showWarning(int idMessage) {
        Toast.makeText(getApplicationContext(), getString(idMessage), Toast.LENGTH_SHORT).show();
    }



}
