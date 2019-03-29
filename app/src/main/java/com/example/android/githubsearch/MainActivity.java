package com.example.android.githubsearch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements SongSearchAdapter.OnSearchItemClickListener, LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SONGS_ARRAY_KEY = "songLyrics";
    private static final String SEARCH_URL_KEY = "songSearchURL";

    private static final int LYRIC_SEARCH_LOADER_ID = 0;

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private TextView mLoadingErrorTV;
    private ProgressBar mLoadingPB;
    private SongSearchAdapter mSongSearchAdapter;

    private LyricUtils.Song[] mSongs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = findViewById(R.id.et_search_box);
        mSearchResultsRV = findViewById(R.id.rv_search_results);
        mLoadingErrorTV = findViewById(R.id.tv_loading_error);
        mLoadingPB = findViewById(R.id.pb_loading);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mSongSearchAdapter = new SongSearchAdapter(this);
        mSearchResultsRV.setAdapter(mSongSearchAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(SONGS_ARRAY_KEY)) {
            mSongs = (LyricUtils.Song[]) savedInstanceState.getSerializable(SONGS_ARRAY_KEY);
            mSongSearchAdapter.updateSearchResults(mSongs);
        }

        getSupportLoaderManager().initLoader(LYRIC_SEARCH_LOADER_ID, null, this);

        Button searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doSongSearch(searchQuery);
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

    private void doSongSearch(String query) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String sort = preferences.getString(
//                getString(R.string.pref_sort_key), getString(R.string.pref_sort_default)
//        );
//        String language = preferences.getString(
//                getString(R.string.pref_language_key), getString(R.string.pref_language_default)
//        );
//        String user = preferences.getString(getString(R.string.pref_user_key), "");
//        boolean searchInName = preferences.getBoolean(getString(R.string.pref_in_name_key), true);
//        boolean searchInDescription = preferences.getBoolean(getString(R.string.pref_in_description_key), true);
//        boolean searchInReadme = preferences.getBoolean(getString(R.string.pref_in_readme_key), false);

        String url = LyricUtils.buildLyricSearchURL(query);
        Log.d(TAG, "querying search URL: " + url);

        Bundle args = new Bundle();
        args.putString(SEARCH_URL_KEY, url);
        mLoadingPB.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(LYRIC_SEARCH_LOADER_ID, args, this);
    }

    @Override
    public void onSearchItemClick(LyricUtils.Song song) {
        Intent intent = new Intent(this, RepoDetailActivity.class);
        intent.putExtra(LyricUtils.EXTRA_LYRIC_REPO, song);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSongs != null) {
            outState.putSerializable(SONGS_ARRAY_KEY, mSongs);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String url = null;
        if (bundle != null) {
            url = bundle.getString(SEARCH_URL_KEY);
        }
        return new SongSearchLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        Log.d(TAG, "loader finished loading");
        if (s != null) {
            mLoadingErrorTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            mSongs = LyricUtils.parseSongSearchResults(s);
            mSongSearchAdapter.updateSearchResults(mSongs);
        } else {
            mLoadingErrorTV.setVisibility(View.VISIBLE);
            mSearchResultsRV.setVisibility(View.INVISIBLE);
        }
        mLoadingPB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Nothing to do here...
    }
}