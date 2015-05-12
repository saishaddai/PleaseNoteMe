package com.nearsoft.pleasenoteme.bean;

public class Dictionary {

    private String word;
    private String meanings;

    public Dictionary(){}

    public Dictionary(String word, String meanings) {
        this.word= word;
        this.meanings = meanings;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeanings() {
        return meanings;
    }

    public void setMeanings(String meanings) {
        this.meanings = meanings;
    }
}
