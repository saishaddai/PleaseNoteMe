package com.nearsoft.pleasenoteme.utils;

public class StringUtilities {

    public static String sanitizeKeyWord(String keyword) {
        if(keyword == null) {
            return "";
        }
        return keyword.replaceAll("[^\\p{L}\\p{Nd}]+", "").toLowerCase();
    }

}
