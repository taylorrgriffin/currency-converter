package com.example.android.githubsearch;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrUtils {
    private final static String CURR_SEARCH_BASE_URL = "https://api.exchangeratesapi.io/latest";
    private final static String CURR_SEARCH_BASE_PARAM = "base";

    public static final String EXTRA_CURR_REPO = "CurrUtils.Lyric";

    public static class CurrConversionResults {
        public Rate rates;
    }

    public static class Rate implements Serializable {
        public String BGN;
        public String NZD;
        public String ILS;
        public String RUB;
        public String CAD;
        public String USD;
        public String PHP;
        public String CHF;
        public String AUD;
        public String JPY;
        public String TRY;
        public String HKD;
        public String MYR;
        public String HRK;
        public String CZK;
        public String IDR;
        public String DKK;
        public String NOK;
        public String HUF;
        public String GBP;
        public String MXN;
        public String THB;
        public String ISK;
        public String ZAR;
        public String BRL;
        public String SGD;
        public String PLN;
        public String INR;
        public String KRW;
        public String RON;
        public String CNY;
        public String SEK;
        public String EUR;
    }

    public static String buildConversionSearchURL(String base) {
        return Uri.parse(CURR_SEARCH_BASE_URL).buildUpon()
                .appendQueryParameter(CURR_SEARCH_BASE_PARAM, base)
                .build()
                .toString();
    }

    public static Rate parseCurrConversionResults(String json) {
        Gson gson = new Gson();
        CurrConversionResults results = gson.fromJson(json, CurrConversionResults.class);
        if (results != null && results.rates != null) {
            return results.rates;
        } else {
            return null;
        }
    }
}
