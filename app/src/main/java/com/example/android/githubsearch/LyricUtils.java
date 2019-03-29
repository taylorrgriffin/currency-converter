package com.example.android.githubsearch;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class LyricUtils {
    private final static String LYRIC_SEARCH_BASE_URL = "https://api.audd.io/findLyrics";
    private final static String LYRIC_SEARCH_QUERY_PARAM = "q";

    public static final String EXTRA_LYRIC_REPO = "LyricUtils.Lyric";

    public static class Song implements Serializable {
        public String title_with_featured;
        public String artist;
        public String lyrics;
    }

    public static class SongSearchResults {
        public Song[] items;
    }

    public static String buildLyricSearchURL(String query) {
        return Uri.parse(LYRIC_SEARCH_BASE_URL).buildUpon()
                .appendQueryParameter(LYRIC_SEARCH_QUERY_PARAM, query)
                .build()
                .toString();
    }

    public static Song[] parseSongSearchResults(String json) {
        Gson gson = new Gson();
        SongSearchResults results = gson.fromJson(json, SongSearchResults.class);
        if (results != null && results.items != null) {
            return results.items;
        } else {
            return null;
        }
    }
}
