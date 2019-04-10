package com.example.android.currencyconverter;

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
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
    private TextView mNetworkErrorTV;
    private TextView mInputErrorTV;
    private ProgressBar mLoadingPB;
    private TextView mLowPercentageTV;
    private TextView mMidPercentageTV;
    private TextView mHighPercentageTV;
    private LinearLayout mSlidingTipViewLL;
    private Button mTipButtonBT;

    private CurrUtils.Rate mRate;

    private boolean isUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isUp = false;

        mNetworkErrorTV = findViewById(R.id.tv_network_error);
        mInputErrorTV = findViewById(R.id.tv_input_error);
        mLoadingPB = findViewById(R.id.pb_loading);
        mInputValueET = findViewById(R.id.et_input_curr);
        mBaseCurrSP = findViewById(R.id.sp_base_curr);
        mDestCurrSP = findViewById(R.id.sp_dest_curr);
        mResultTV = findViewById(R.id.tv_conversion_result);
        mLowPercentageTV = findViewById(R.id.tv_low_percentage);
        mMidPercentageTV = findViewById(R.id.tv_mid_percentage);
        mHighPercentageTV = findViewById(R.id.tv_high_percentage);
        mSlidingTipViewLL = findViewById(R.id.sliding_tip_view);

        mSlidingTipViewLL.setVisibility(View.INVISIBLE);

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
                "HKD",
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
                "HKD",
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

        ArrayAdapter<String> baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, baseItems);
        ArrayAdapter<String> destAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, destItems);

        mBaseCurrSP.setAdapter(baseAdapter);
        mDestCurrSP.setAdapter(destAdapter);

        mBaseCurrSP.setSelection(0);
        mDestCurrSP.setSelection(0);

        Button searchButton = findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mInputErrorTV.setVisibility(View.INVISIBLE);
                mNetworkErrorTV.setVisibility(View.INVISIBLE);
                mResultTV.setVisibility(View.INVISIBLE);

                if (mBaseCurrSP.getSelectedItemPosition() != 0
                        && mDestCurrSP.getSelectedItemPosition() != 0
                        && !TextUtils.isEmpty(mInputValueET.getText())){
                    getCurrRates(mBaseCurrSP.getSelectedItem().toString());
                } else{

                    mInputErrorTV.setVisibility(View.VISIBLE);

                }

            }
        });

        mTipButtonBT = findViewById(R.id.btn_tip);
        mTipButtonBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSlideViewButtonClick(v);

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

        String[] nations = new String[]{
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
                "HKD",
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

        String[] symbols = new String[]{
                "лв",
                "$",
                "₪",
                "₱",
                "$",
                "$",
                "₱",
                "CHF",
                "$",
                "¥",
                "₺",
                "$",
                "RM",
                "kn",
                "Kč",
                "Rp",
                "kr",
                "kr",
                "Ft",
                "£",
                "$",
                "฿",
                "kr",
                "R",
                "R$",
                "$",
                "zł",
                "₹",
                "₩",
                "lei",
                "¥",
                "kr",
                "€"
        };

        HashMap<String, String> symbol_map = new HashMap<>();

        for(int i = 0; i< nations.length; i++){
            symbol_map.put(nations[i], symbols[i]);
        }


        if (s != null) {

            mInputErrorTV.setVisibility(View.INVISIBLE);
            mNetworkErrorTV.setVisibility(View.INVISIBLE);
            mRate = CurrUtils.parseCurrConversionResults(s);
            String dest = mDestCurrSP.getSelectedItem().toString();
            float rate = CurrUtils.getConversion(mRate, dest);
            String symbol = symbol_map.get(dest);

            // Check that rate was a non-zero float.
            if (rate == 0.0){
                mNetworkErrorTV.setVisibility(View.VISIBLE);
            } else{

                try{

                    DecimalFormat df = new DecimalFormat("#.##");
                    float initial_value = Float.parseFloat(mInputValueET.getText().toString());
                    float converted_value = rate * initial_value;
                    mResultTV.setVisibility(View.VISIBLE);
                    mResultTV.setText(symbol + " " + df.format(converted_value));

                    ArrayList<Float> percentages = CurrUtils.computePercentages((float)0.1, (float)0.15, (float)0.2, converted_value);

                    mLowPercentageTV.setText("10%: " + df.format(percentages.get(0)));
                    mMidPercentageTV.setText("15%: " + df.format(percentages.get(1)));
                    mHighPercentageTV.setText("20%: " + df.format(percentages.get(2)));

                } catch(Exception e){

                    mInputErrorTV.setVisibility(View.VISIBLE);

                }
            }


        } else {
            mNetworkErrorTV.setVisibility(View.VISIBLE);
        }
        mLoadingPB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Nothing to do here...
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        // View initially inivisible; set to visable upon instansiation
        if (view.getVisibility() == View.INVISIBLE){
            view.setVisibility(View.VISIBLE);
        }
        if (isUp) {
            slideDown(mSlidingTipViewLL);
            mTipButtonBT.setText("Easy Tip Calculator");
        } else {
            slideUp(mSlidingTipViewLL);
            mTipButtonBT.setText("Hide");
        }
        isUp = !isUp;
    }

}