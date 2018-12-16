package com.example.muklahhn.popular_movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muklah H N on 21/04/2018.
 */

public class RecyclerViewAdapterReview extends RecyclerView.Adapter<RecyclerViewAdapterReview.RecyclerViewHolder> {

    ArrayList<ReviewItem> mCommentsItems;
    private Context context;

    public RecyclerViewAdapterReview(ArrayList<ReviewItem> commentsItems) {
        mCommentsItems = commentsItems;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public final TextView CommentAuthor;
        public final TextView CommentContent;

        public RecyclerViewHolder(View view) {
            super(view);
            CommentAuthor = (TextView)itemView.findViewById(R.id.comment_author);
            CommentContent = (TextView)itemView.findViewById(R.id.comment_content);
        }
    }

    @Override
    public RecyclerViewAdapterReview.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.comment_list_item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewAdapterReview.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterReview.RecyclerViewHolder holder, int position) {
        holder.CommentAuthor.setText(String.valueOf(mCommentsItems.get(position).getAuthor()));
        holder.CommentContent.setText(String.valueOf(mCommentsItems.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        if (null == mCommentsItems)
            return 0;
        else {
            return mCommentsItems.size();
        }
    }

    public void setCommentData(ArrayList<ReviewItem> commentData) {
        mCommentsItems = commentData;
        notifyDataSetChanged();
    }
}