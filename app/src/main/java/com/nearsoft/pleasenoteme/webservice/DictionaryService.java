package com.nearsoft.pleasenoteme.webservice;

import android.os.StrictMode;
import android.util.Log;

import com.nearsoft.pleasenoteme.bean.Dictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DictionaryService {
    String TAG = DictionaryService.class.getName();

    public Dictionary connectWebService(String keyword, String noDefinitionFoundMessage) throws Exception{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection = null;
        try {
            String url = "http://demo6655573.mockable.io/?word=";
            URL obj = new URL(url + keyword);
            urlConnection = (HttpURLConnection) obj.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br  = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return parseResponse(response.toString(), noDefinitionFoundMessage);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private Dictionary parseResponse(String jsonResponse, String noDefinitionFoundMessage) throws JSONException {
        Dictionary dictionary = new Dictionary();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String dictionaryNode = "definition";
            JSONObject jsonResponseObject =
                    new JSONObject(jsonObject.getString(dictionaryNode));

            String dictionaryWord = "word";
            String dictionaryMeaning = "meanings";
            dictionary.setWord(jsonResponseObject.getString(dictionaryWord));
            JSONArray jsonMeanings = jsonResponseObject.getJSONArray(dictionaryMeaning);
            dictionary.setMeanings(parseMeanings(jsonMeanings));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return new Dictionary(noDefinitionFoundMessage, "");
        }
        return dictionary;
    }

    private String parseMeanings(JSONArray jsonMeanings) throws JSONException {
        if(jsonMeanings == null ) {
            return "";
        }
        StringBuilder meanings = new StringBuilder();
        try {
            for (int i = 0; i < jsonMeanings.length(); ++i) {
                String meaning = (String) jsonMeanings.get(i);
                meanings.append("\n\t\t -");
                meanings.append(meaning);
            }
        } catch(JSONException e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
        return meanings.toString();
    }


}
