package com.nearsoft.pleasenoteme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DictionaryActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.nearsoft.pleasenote.MESSAGE";
    private String url = "http://demo6655573.mockable.io/?word=";
    private final String DICTIONARY_NODE = "dictionary";
    private final String DICTIONARY_WORD = "word";
    private final String DICTIONARY_MEANING = "meaning";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendWordToWS(View view) {
        EditText editText = (EditText) findViewById(R.id.search_word);
        String message = editText.getText().toString();
        TextView definitionTextView = (TextView) findViewById(R.id.definition_component);
        definitionTextView.setText("");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection = null;
        try {
            URL obj = new URL(url + message);
            urlConnection = (HttpURLConnection) obj.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br  = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String printableDefinition = jsonResponseToPrintable(response.toString());
            definitionTextView.setText(printableDefinition);

        } catch(Exception e) {
            Log.e(MainActivity.class.getName(), e.getMessage());
            Toast.makeText(getApplicationContext(),
                    R.string.error_unreachable_ws, Toast.LENGTH_LONG).show();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            callIntent.setClass(DictionaryActivity.this, MainActivity.class);
            startActivity(callIntent);
        } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
        }
    }

    private String jsonResponseToPrintable(String jsonResponse) throws JSONException {
        String printableDefinition = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject jsonResponseObject =
                    new JSONObject(jsonObject.getString(DICTIONARY_NODE));

            printableDefinition = "<b>" + jsonResponseObject.getString(DICTIONARY_WORD) + ":</b><br> " +
                    jsonResponseObject.getString(DICTIONARY_MEANING);

        } catch (JSONException e) {
            Log.w(DictionaryActivity.class.getName(), getString(R.string.error_dictionary_not_found_500));
            return getString(R.string.warning_no_definition_found);
        }
        return printableDefinition;
    }


}