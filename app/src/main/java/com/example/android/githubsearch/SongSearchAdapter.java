package com.example.android.githubsearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SongSearchAdapter extends RecyclerView.Adapter<SongSearchAdapter.SearchResultViewHolder> {
    private LyricUtils.Song[] mSongs;
    OnSearchItemClickListener mSeachItemClickListener;

    public interface OnSearchItemClickListener {
        void onSearchItemClick(LyricUtils.Song song);
    }

    SongSearchAdapter(OnSearchItemClickListener searchItemClickListener) {
        mSeachItemClickListener = searchItemClickListener;
    }

    public void updateSearchResults(LyricUtils.Song[] repos) {
        mSongs = repos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSongs != null) {
            return mSongs.length;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.bind(mSongs[position]);
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = itemView.findViewById(R.id.tv_search_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LyricUtils.Song searchResult = mSongs[getAdapterPosition()];
                    mSeachItemClickListener.onSearchItemClick(searchResult);
                }
            });
        }

        public void bind(LyricUtils.Song song) {
            mSearchResultTV.setText(song.title_with_featured);
        }
    }
}