package com.example.android.githubsearch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CURR_ARRAY_KEY = "currencyConversion";
    private static final String SEARCH_URL_KEY = "currencySearchURL";

    private static final int CURR_SEARCH_LOADER_ID = 0;

    private EditText mInputValueET;
    private Spinner mBaseCurrSP;
    private Spinner mDestCurrSP;
    private TextView mResultTV;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;

    private CurrUtils.Rate mRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingErrorTV = findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);
        mInputValueET = findViewById(R.id.et_input_curr);
        mBaseCurrSP = findViewById(R.id.sp_base_curr);
        mDestCurrSP = findViewById(R.id.sp_dest_curr);
        mResultTV = findViewById(R.id.tv_conversion_result);

        //Populate Spinners with conversion options.
        String[] baseItems = new String[]{
                "From:",
                "BGN",
                "NZD",
                "ILS",
                "RUB",
                "CAD",
                "USD",
                "PHP",
                "CHF",
                "AUD",
                "JPY",
                "TRY",
                "HDK",
                "MYR",
                "HRK",
                "CZK",
                "IDR",
                "DKK",
                "NOK",
                "HUF",
                "GBP",
                "MXN",
                "THB",
                "ISK",
                "ZAR",
                "BRL",
                "SGD",
                "PLN",
                "INR",
                "KRW",
                "RON",
                "CNY",
                "SEK",
                "EUR"
        };

        String[] destItems = new String[]{
                "To:",
                "BGN",
                "NZD",
                "ILS",
                "RUB",
                "CAD",
                "USD",
                "PHP",
                "CHF",
                "AUD",
                "JPY",
                "TRY",
                "HDK",
                "MYR",
                "HRK",
                "CZK",
                "IDR",
                "DKK",
                "NOK",
                "HUF",
                "GBP",
                "MXN",
                "THB",
                "ISK",
                "ZAR",
                "BRL",
                "SGD",
                "PLN",
                "INR",
                "KRW",
                "RON",
                "CNY",
                "SEK",
                "EUR"
        };

        ArrayAdapter<String> baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, baseItems);
        ArrayAdapter<String> destAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destItems);

        mBaseCurrSP.setAdapter(baseAdapter);
        mDestCurrSP.setAdapter(destAdapter);

        mBaseCurrSP.setSelection(0);
        mDestCurrSP.setSelection(0);

        Button searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaseCurrSP.getSelectedItemPosition() != 0
                        && mDestCurrSP.getSelectedItemPosition() != 0
                        && !TextUtils.isEmpty(mInputValueET.getText())){
                    getCurrRates(mBaseCurrSP.getSelectedItem().toString());
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getCurrRates(String base) {
        String url = CurrUtils.buildConversionSearchURL(base);
        Log.d(TAG, "querying search URL: " + url);
        Bundle args = new Bundle();
        args.putString(SEARCH_URL_KEY, url);
        mLoadingPB.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(CURR_SEARCH_LOADER_ID, args, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String url = null;
        if (bundle != null) {
            url = bundle.getString(SEARCH_URL_KEY);
        }
        return new CurrSearchLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        Log.d(TAG, "loader finished loading");
        if (s != null) {
            mLoadingErrorTV.setVisibility(View.INVISIBLE);
            //mResultBoxTV.setVisibility(View.VISIBLE);
            mRate = CurrUtils.parseCurrConversionResults(s);
            Log.d(TAG, "RATE is: "+mRate);
        } else {
            mLoadingErrorTV.setVisibility(View.VISIBLE);
            //mResultBoxTV.setVisibility(View.INVISIBLE);
        }
        mLoadingPB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Nothing to do here...
    }
}