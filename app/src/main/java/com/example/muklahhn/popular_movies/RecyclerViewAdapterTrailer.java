package com.example.muklahhn.popular_movies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muklah H N on 15/09/2017.
 */

public class RecyclerViewAdapterTrailer extends RecyclerView.Adapter<RecyclerViewAdapterTrailer.RecyclerViewHolder> {

    ArrayList<TrailerItem> mVideosItems;
    private Context context;
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    public static final String YOUTUBE_INTENT = "vnd.youtube";

    private final RecyclerViewAdapterTrailerOnClickHandler mClickHandler;

    public interface RecyclerViewAdapterTrailerOnClickHandler {
        void onClick(TrailerItem video);
    }

    public RecyclerViewAdapterTrailer(RecyclerViewAdapterTrailerOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView VideoId;
        public final TextView VideoName;
        public ImageView VideoImage;

        public RecyclerViewHolder(View view) {
            super(view);
            VideoId = (TextView)itemView.findViewById(R.id.video_id);
            VideoName = (TextView)itemView.findViewById(R.id.video_name);
            VideoImage = (ImageView)itemView.findViewById(R.id.video_image);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TrailerItem video = mVideosItems.get(adapterPosition);
            mClickHandler.onClick(video);
            final String trailerId = mVideosItems.get(adapterPosition).getKey();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_INTENT + ":" + trailerId));
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + trailerId));
                context.startActivity(intent);
            }
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.VideoId.setText(String.valueOf(mVideosItems.get(position).getId()));
        holder.VideoName.setText(String.valueOf(mVideosItems.get(position).getName()));
    }


    @Override
    public int getItemCount() {
        if (null == mVideosItems)
            return 0;
        else {
            return mVideosItems.size();
        }
    }

    public void setVideoData(ArrayList<TrailerItem> videoData) {
        mVideosItems = videoData;
        notifyDataSetChanged();
    }

}