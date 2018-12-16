package com.example.muklahhn.popular_movies;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muklahhn.popular_movies.data.MoviesContract;
import com.example.muklahhn.popular_movies.utilities.NetworkUtilsReview;
import com.example.muklahhn.popular_movies.utilities.NetworkUtilsTrailer;
import com.example.muklahhn.popular_movies.utilities.OpenCommentJsonUtilsReview;
import com.example.muklahhn.popular_movies.utilities.OpenVideoJsonUtilsTrailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements
        RecyclerViewAdapterTrailer.RecyclerViewAdapterTrailerOnClickHandler {

    RecyclerViewAdapterTrailer mAdapterTrailer;
    RecyclerViewAdapterReview mAdapterReview;

    int MovieId;
    Double MovieVoteAverage;
    String MoviePoster;
    String MovieName;
    String MovieOverview;
    String MovieDate;

    private static final ArrayList<ReviewItem> NUM_LIST_REVIEWS = null;

    @BindView(R.id.iv_poster)
    ImageView mMoviePoster;
    @BindView(R.id.tv_movie_name)
    TextView mMovieName;
    @BindView(R.id.tv_movie_date)
    TextView mMovieDate;
    @BindView(R.id.tv_vote_average)
    TextView mVoteAverage;
    @BindView(R.id.tv_movie_overview)
    TextView mMovieOverview;
    @BindView(R.id.tv_movie_id)
    TextView mMovieId;

    @BindView(R.id.rv_videos)
    RecyclerView mVideosList;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsList;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.trailers)
    TextView mErrorTrailers;
    @BindView(R.id.reviews)
    TextView mErrorReviews;

    @BindView(R.id.pb_loading_indicator_trailers)
    ProgressBar mLoadingIndicatorTrailers;
    @BindView(R.id.pb_loading_indicator_reviews)
    ProgressBar mLoadingIndicatorReviews;
    @BindView(R.id.favorite)
    ImageButton mFavorite;

    private static final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        MovieItem movieObject = getIntent().getParcelableExtra("movie");

        if (movieObject != null) {

            MovieId = movieObject.getId();
            MovieVoteAverage = movieObject.getAverage();
            MoviePoster = movieObject.getPhoto();
            MovieName = movieObject.getName();
            MovieOverview = movieObject.getDescription();
            MovieDate = movieObject.getDate();

            Picasso.with(this)
                    .load(movieObject.getFullPosterPath())
                    .placeholder(R.drawable.three)
                    .error(R.drawable.three)
                    .resize(700, 1000)
                    .into(mMoviePoster);
            mMovieName.setText(String.valueOf(MovieName));
            mMovieDate.setText(String.valueOf(MovieDate));
            mVoteAverage.setText(String.valueOf(MovieVoteAverage));
            mMovieOverview.setText(String.valueOf(MovieOverview));
            mMovieId.setText(String.valueOf(MovieId));
        }

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mVideosList.setLayoutManager(LayoutManager);
        mVideosList.setHasFixedSize(true);
        mAdapterTrailer = new RecyclerViewAdapterTrailer(this);
        mVideosList.setAdapter(mAdapterTrailer);

        LinearLayoutManager LayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsList.setLayoutManager(LayoutManager2);
        mReviewsList.setHasFixedSize(true);
        mAdapterReview = new RecyclerViewAdapterReview(NUM_LIST_REVIEWS);
        mReviewsList.setAdapter(mAdapterReview);

        loadVideosData(String.valueOf(MovieId));
        loadReviewsData(String.valueOf(MovieId));

        final String[] FAVORITE_MOVIES_PROJECTION = {
                MoviesContract.MoviesEntry.COLUMN_MOVIES_ID
        };

        Uri uri = MoviesContract.MoviesEntry.buildMoviesUriWithId(MovieId);

        Cursor fmovie = getContentResolver().query(uri,
                FAVORITE_MOVIES_PROJECTION,
                null,
                null,
                null);

        if (fmovie.getCount() != 0) {
            mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
        } else {
            mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mVideosList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        Parcelable listState2 = mReviewsList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mVideosList.getLayoutManager().onRestoreInstanceState(listState);
            Parcelable listState2 = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mReviewsList.getLayoutManager().onRestoreInstanceState(listState2);
        }
    }

    private void loadVideosData(String movieId) {
        showVideoDataView();
        new FetchVideosTask().execute(movieId);
    }

    private void loadReviewsData(String movieId) {
        showReviewDataView();
        new FetchReviewsTask().execute(movieId);
    }

    public void onClickFavorite(View view) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIES_ID, MovieId);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, MovieVoteAverage);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, MoviePoster);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, MovieName);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, MovieOverview);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, MovieDate);

        final String[] FAVORITE_MOVIES_DETAIL_PROJECTION = {
                MoviesContract.MoviesEntry.COLUMN_MOVIES_ID,
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE
        };

        Uri truuri = MoviesContract.MoviesEntry.buildMoviesUriWithId(MovieId);

        Cursor data = getContentResolver().query(truuri,
                FAVORITE_MOVIES_DETAIL_PROJECTION,
                null,
                null,
                null);

        if (data.getCount() == 0) {

            Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
            String text = "Added To Favorites";
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();

        } else {
            String text = "already Added To Favorites";
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(TrailerItem video) {

    }

    private void showVideoDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mVideosList.setVisibility(View.VISIBLE);
    }

    private void showReviewDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mReviewsList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mVideosList.setVisibility(View.INVISIBLE);
        mReviewsList.setVisibility(View.INVISIBLE);
        mErrorTrailers.setVisibility(View.INVISIBLE);
        mErrorReviews.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchVideosTask extends AsyncTask<String, Void, ArrayList<TrailerItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorTrailers.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<TrailerItem> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String movieId = params[0];
            URL videosRequestUrl = NetworkUtilsTrailer.buildUrl(movieId);

            try {
                String jsonVideoResponse = NetworkUtilsTrailer.getResponseFromHttpUrl(videosRequestUrl);

                ArrayList<TrailerItem> simpleJsonVideoData = OpenVideoJsonUtilsTrailer.getSimpleVideoStringsFromJson(DetailActivity.this, jsonVideoResponse);

                return simpleJsonVideoData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerItem> videoData) {
            mLoadingIndicatorTrailers.setVisibility(View.INVISIBLE);
            if (videoData != null) {
                showVideoDataView();
                mAdapterTrailer.setVideoData(videoData);
            } else {
                showErrorMessage();
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<ReviewItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorReviews.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ReviewItem> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String movieId = params[0];
            URL reviewsRequestUrl = NetworkUtilsReview.buildUrl(movieId);

            try {
                String jsonReviewResponse = NetworkUtilsReview.getResponseFromHttpUrl(reviewsRequestUrl);

                ArrayList<ReviewItem> simpleJsonReviewData = OpenCommentJsonUtilsReview.getSimpleCommentStringsFromJson(DetailActivity.this, jsonReviewResponse);

                return simpleJsonReviewData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewItem> reviewData) {
            mLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
            if (reviewData != null) {
                showReviewDataView();
                mAdapterReview.setCommentData(reviewData);
            } else {
                showErrorMessage();
            }
        }
    }

}