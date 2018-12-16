package com.example.muklahhn.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.muklahhn.popular_movies.utilities.NetworkUtils;
import com.example.muklahhn.popular_movies.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerViewAdapter mAdapter;
    RecyclerView mMoviesList;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private static final int MOVIES_LOADER_ID = 0;
    private static final String SORT_ORDER_EXTRA = "";

    private static final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager LayoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(LayoutManager);
        mMoviesList.setHasFixedSize(true);
        mAdapter = new RecyclerViewAdapter(this);
        mMoviesList.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int loaderId = MOVIES_LOADER_ID;
        String sortOrder = "popular";

        LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(SORT_ORDER_EXTRA, sortOrder);
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mMoviesList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mMoviesList.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void loadMoviesData(String sortOrder) {
        String sortOrderExtra = sortOrder;

        Bundle moviesBundle = new Bundle();

        moviesBundle.putString(SORT_ORDER_EXTRA, sortOrderExtra.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<MovieItem>> moviesLoader = loaderManager.getLoader(MOVIES_LOADER_ID);

        if (moviesLoader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, moviesBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, moviesBundle, this);
        }
    }

    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<ArrayList<MovieItem>>(this) {

        ArrayList<MovieItem> mMovieData = null;

        @Override
        protected void onStartLoading() {
            if (mMovieData != null) {
                deliverResult(mMovieData);
            } else {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }
        }

        @Override
        public ArrayList<MovieItem> loadInBackground() {

            String sortOrder = loaderArgs.getString(SORT_ORDER_EXTRA);

            URL moviesRequestUrl = NetworkUtils.buildUrl(sortOrder);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                ArrayList<MovieItem> simpleJsonMovieData = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    public void deliverResult(ArrayList<MovieItem> data) {
        mMovieData = data;
        super.deliverResult(data);
    }
  };
 }

   @Override
     public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(data);
        if (null == data) {
        showErrorMessage();
        } else {
        showMovieDataView();
        }
        }

  @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {
        }

        private void invalidateData() {
         mAdapter.setMovieData(null);
        }

  @Override
    public void onClick(MovieItem movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartDetailActivity);
        }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.sort, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.most_popular) {
                String sortOrder="popular";
                loadMoviesData(sortOrder);
                return true;
            }

            if (id == R.id.top_rated) {
                String sortOrder="top_rated";
                loadMoviesData(sortOrder);
                return true;
            }

            if (id == R.id.favorites) {
                Intent favoritesIntent = new Intent(MainActivity.this, Favorites.class);
                MainActivity.this.startActivity(favoritesIntent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
}