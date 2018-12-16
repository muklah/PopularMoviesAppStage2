package com.example.muklahhn.popular_movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Muklah H N on 15/09/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    ArrayList<MovieItem> mMoviesItems;
    private Context context;
    private final RecyclerViewAdapterOnClickHandler mClickHandler;

    public interface RecyclerViewAdapterOnClickHandler {
        void onClick(MovieItem movie);
    }

    public RecyclerViewAdapter(RecyclerViewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView MoviePoster;

        public RecyclerViewHolder(View view) {
            super(view);
            MoviePoster = (ImageView)itemView.findViewById(R.id.iv_item_movie);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieItem movie = mMoviesItems.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Picasso.with(this.context).load(mMoviesItems.get(position).getFullPosterPath()).placeholder(R.drawable.three)
                .error(R.drawable.three).into(holder.MoviePoster);
    }


    @Override
    public int getItemCount() {
        if (null == mMoviesItems)
            return 0;
        else {
            return mMoviesItems.size();
        }
    }

    public void setMovieData(ArrayList<MovieItem> movieData) {
        mMoviesItems = movieData;
        notifyDataSetChanged();
    }

}