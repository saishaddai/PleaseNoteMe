package com.nearsoft.pleasenoteme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nearsoft.pleasenoteme.bean.Dictionary;
import com.nearsoft.pleasenoteme.utils.StringUtilities;
import com.nearsoft.pleasenoteme.webservice.DictionaryService;


public class DictionaryActivity extends Activity {

    DictionaryService dictionaryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        dictionaryService = new DictionaryService();
    }

    public void sendWordToWS(View view) {
        EditText editText = (EditText) findViewById(R.id.search_word);
        String keyword = editText.getText().toString();
        keyword = StringUtilities.sanitizeKeyWord(keyword);
        if(keyword.isEmpty() || keyword.length() > 50) {
            Toast.makeText(getApplicationContext(),
                    R.string.warning_keyword_validation_failed, Toast.LENGTH_LONG).show();
            return;
        }
        TextView wordTextView = (TextView) findViewById(R.id.definition_word);
        wordTextView.setText("");

        TextView meaningsTextView = (TextView) findViewById(R.id.definition_meanings);
        meaningsTextView.setText("");

        try {
            Dictionary dictionary = dictionaryService.connectWebService(keyword, getString(R.string.warning_no_definition_found));
            wordTextView.setText(dictionary.getWord());
            meaningsTextView.setText(dictionary.getMeanings());
        } catch (Exception e) {
            Log.e(MainActivity.class.getName(), e.getMessage());
            Toast.makeText(getApplicationContext(),
                    R.string.error_unreachable_ws, Toast.LENGTH_SHORT).show();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            callIntent.setClass(DictionaryActivity.this, MainActivity.class);
            startActivity(callIntent);
        }
    }



}
