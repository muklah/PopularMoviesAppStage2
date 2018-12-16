package com.example.muklahhn.popular_movies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.muklahhn.popular_movies.utilities.NetworkUtilsReview;
import com.example.muklahhn.popular_movies.utilities.NetworkUtilsTrailer;
import com.example.muklahhn.popular_movies.utilities.OpenCommentJsonUtilsReview;
import com.example.muklahhn.popular_movies.utilities.OpenVideoJsonUtilsTrailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class review extends AppCompatActivity {

    RecyclerViewAdapterReview mAdapter;
    RecyclerView mCommentsList;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    int MovieId;
    private static final ArrayList<ReviewItem> NUM_LIST_REVIEWS = null;

    @BindView(R.id.iv_poster) ImageView mMoviePoster;
    @BindView(R.id.tv_movie_name) TextView mMovieName;
    @BindView(R.id.tv_movie_date) TextView mMovieDate;
    @BindView(R.id.tv_vote_average) TextView mVoteAverage;
    @BindView(R.id.tv_movie_overview) TextView mMovieOverview;
    @BindView(R.id.tv_movie_id) TextView mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        MovieItem movieObject = getIntent().getParcelableExtra("movie");

        if (movieObject != null) {

            MovieId = movieObject.getId();

            Picasso.with(this)
                    .load(movieObject.getFullPosterPath())
                    .placeholder(R.drawable.three)
                    .error(R.drawable.three)
                    .resize(700,1000)
                    .into(mMoviePoster);
            mMovieName.setText(String.valueOf(movieObject.getName()));
            mMovieDate.setText(String.valueOf(movieObject.getDate()));
            mVoteAverage.setText(String.valueOf(movieObject.getAverage()));
            mMovieOverview.setText(String.valueOf(movieObject.getDescription()));
            mMovieId.setText(String.valueOf(movieObject.getId()));
        }

        mCommentsList = (RecyclerView) findViewById(R.id.rv_comments);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mCommentsList.setLayoutManager(LayoutManager);

        mCommentsList.setHasFixedSize(true);

        mAdapter = new RecyclerViewAdapterReview(NUM_LIST_REVIEWS);

        mCommentsList.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadCommentsData(String.valueOf(MovieId));
    }

    private void loadCommentsData(String movieId) {
        showCommentDataView();
        new review.FetchCommentsTask().execute(movieId);
    }

    private void showCommentDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mCommentsList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mCommentsList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchCommentsTask extends AsyncTask<String, Void, ArrayList<ReviewItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ReviewItem> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String movieId = params[0];
            URL commentsRequestUrl = NetworkUtilsReview.buildUrl(movieId);

            try {
                String jsonCommentResponse = NetworkUtilsReview.getResponseFromHttpUrl(commentsRequestUrl);

                ArrayList<ReviewItem> simpleJsonCommentData = OpenCommentJsonUtilsReview.getSimpleCommentStringsFromJson(review.this, jsonCommentResponse);

                return simpleJsonCommentData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewItem> commentData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (commentData != null) {
                showCommentDataView();
                mAdapter.setCommentData(commentData);
            } else {
                showErrorMessage();
            }
        }
    }

}
