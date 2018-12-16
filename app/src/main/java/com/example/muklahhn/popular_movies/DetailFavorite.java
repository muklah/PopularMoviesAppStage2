package com.example.muklahhn.popular_movies;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muklahhn.popular_movies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;

public class DetailFavorite extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] FAVORITE_MOVIES_DETAIL_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIES_ID,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
            MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE
    };

    public static final int INDEX_MOVIES_ID = 0;
    public static final int INDEX_VOTE_AVERAGE = 1;
    public static final int INDEX_POSTER_PATH = 2;
    public static final int INDEX_ORIGINAL_TITLE = 3;
    public static final int INDEX_OVERVIEW = 4;
    public static final int INDEX_RELEASE_DATE = 5;

    private static final int ID_DETAIL_LOADER = 999;

    private Uri mUri;

    @BindView(R.id.tv_vote_average)
    TextView mMovieVoteAverage;
    @BindView(R.id.iv_poster)
    ImageView mMoviePoster;
    @BindView(R.id.tv_movie_name)
    TextView mMovieName;
    @BindView(R.id.tv_movie_overview)
    TextView mMovieOverview;
    @BindView(R.id.tv_movie_date)
    TextView mMovieDate;
    @BindView(R.id.favorite)
    ImageButton mFavorite;

    private static final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));

    }

    public void onClickFavorite(View view) {
        String favoriteMovieId = mUri.getLastPathSegment();

        Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(favoriteMovieId).build();
        getContentResolver().delete(uri, null, null);
        String text = "Deleted From Favorites";
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        FAVORITE_MOVIES_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {

            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {

            return;
        }

        int movieIdIndex = data.getInt(INDEX_MOVIES_ID);
        double movieVoteAverageIndex = data.getDouble(INDEX_VOTE_AVERAGE);
        String moviePosterPathIndex = data.getString(INDEX_POSTER_PATH);
        String movieNameIndex = data.getString(INDEX_ORIGINAL_TITLE);
        String movieOverviewIndex = data.getString(INDEX_OVERVIEW);
        String movieReleaseDateIndex = data.getString(INDEX_RELEASE_DATE);

        String posterLinkPath = "http://image.tmdb.org/t/p/w342";
        String fullPosterPath = posterLinkPath + moviePosterPathIndex;

        mMovieVoteAverage.setText(String.valueOf(movieVoteAverageIndex));
        Picasso.with(this).load(fullPosterPath).placeholder(R.drawable.three)
                .error(R.drawable.three).resize(700, 1000).into(mMoviePoster);
        mMovieName.setText(movieNameIndex);
        mMovieOverview.setText(movieOverviewIndex);
        mMovieDate.setText(movieReleaseDateIndex);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
