//
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//public class CurrSearchAdapter extends RecyclerView.Adapter<CurrSearchAdapter.SearchResultViewHolder> {
//    private CurrUtils.Rate mRate;
//
//    public void updateSearchResults(CurrUtils.Rate rate) {
//        mRate = rate;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.search_result_item, parent, false);
//        return new SearchResultViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
//        holder.bind(mSongs[position]);
//    }
//
//    class SearchResultViewHolder extends RecyclerView.ViewHolder {
//        private TextView mSearchResultTV;
//
//        public SearchResultViewHolder(View itemView) {
//            super(itemView);
//            mSearchResultTV = itemView.findViewById(R.id.tv_search_result);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CurrUtils.Song searchResult = mSongs[getAdapterPosition()];
//                    mSeachItemClickListener.onSearchItemClick(searchResult);
//                }
//            });
//        }
//
//        public void bind(CurrUtils.Song song) {
//            mSearchResultTV.setText(song.title_with_featured);
//        }
//    }
//}