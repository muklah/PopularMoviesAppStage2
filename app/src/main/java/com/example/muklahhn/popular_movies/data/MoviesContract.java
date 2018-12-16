
package com.example.muklahhn.popular_movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.muklahhn.popular_movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIES_ID = "movies_id";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildMoviesUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

        public static String getSqlSelectForTodayOnwards() {
            return MoviesEntry.COLUMN_MOVIES_ID + " >= ";
        }

    }
}