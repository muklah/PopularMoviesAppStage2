
package com.example.muklahhn.popular_movies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.muklahhn.popular_movies.data.MoviesContract;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdapterFavorite extends RecyclerView.Adapter<RecyclerViewAdapterFavorite.FavoritesViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private final RecyclerViewAdapterFavorite.RecyclerViewAdapterOnClickHandler mClickHandler;

    public interface RecyclerViewAdapterOnClickHandler {
        void onClick(long id);
    }

    public RecyclerViewAdapterFavorite(@NonNull Context context, RecyclerViewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView favoriteMovieId;
        ImageView favoriteMoviePoster;
        TextView favoriteMovieName;

        public FavoritesViewHolder(View itemView) {
            super(itemView);

            favoriteMovieId = (TextView) itemView.findViewById(R.id.movie_id);
            favoriteMoviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            favoriteMovieName = (TextView) itemView.findViewById(R.id.movie_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int movieIdIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIES_ID);
            long id = mCursor.getLong(movieIdIndex);
            mClickHandler.onClick(id);
        }
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.favorite_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        int idIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry._ID);
        int movieIdIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIES_ID);
        int moviePosterPathIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
        int movieNameIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE);

        final int id = mCursor.getInt(idIndex);
        int movieId = mCursor.getInt(movieIdIndex);
        String moviePosterPath = mCursor.getString(moviePosterPathIndex);
        String movieName = mCursor.getString(movieNameIndex);

        String posterLinkPath = "http://image.tmdb.org/t/p/w342";
        String fullPosterPath = posterLinkPath + moviePosterPath;

        holder.itemView.setTag(id);
        String movieIdString = "" + movieId;
        holder.favoriteMovieId.setText(movieIdString);
        Picasso.with(this.mContext).load(fullPosterPath).placeholder(R.drawable.three)
                .error(R.drawable.three).into(holder.favoriteMoviePoster);
        holder.favoriteMovieName.setText(movieName);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {

        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}