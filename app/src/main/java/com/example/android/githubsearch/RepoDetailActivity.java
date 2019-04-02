//package com.example.android.githubsearch;
//
//import android.content.Intent;
//import android.support.v4.app.ShareCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//
//public class RepoDetailActivity extends AppCompatActivity {
//    private TextView mRepoNameTV;
//    private TextView mRepoStarsTV;
//    private TextView mRepoDescriptionTV;
//    private CurrUtils.Song mSongs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_repo_detail);
//
//        mRepoNameTV = findViewById(R.id.tv_repo_name);
//        mRepoStarsTV = findViewById(R.id.tv_repo_stars);
//        mRepoDescriptionTV = findViewById(R.id.tv_repo_description);
//
//        mSongs = null;
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(CurrUtils.EXTRA_LYRIC_REPO)) {
//            mSongs = (CurrUtils.Song) intent.getSerializableExtra(CurrUtils.EXTRA_LYRIC_REPO);
//            mRepoNameTV.setText(mSongs.title_with_featured);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.repo_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_view_on_web:
//                return true;
//            case R.id.action_share:
//                shareRepo();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public void shareRepo() {
//        if (mSongs != null) {
//            String shareText = mSongs.title_with_featured;
//            ShareCompat.IntentBuilder.from(this)
//                    .setType("text/plain")
//                    .setText(shareText)
//                    .setChooserTitle(R.string.share_chooser_title)
//                    .startChooser();
//        }
//    }
//}
