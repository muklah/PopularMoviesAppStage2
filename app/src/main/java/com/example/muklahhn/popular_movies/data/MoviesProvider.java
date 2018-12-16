package com.example.muklahhn.popular_movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.example.muklahhn.popular_movies.data.MoviesContract.MoviesEntry.COLUMN_MOVIES_ID;
import static com.example.muklahhn.popular_movies.data.MoviesContract.MoviesEntry.TABLE_NAME;

/**
 * Created by Muklah H N on 23/04/2018.
 */

public class MoviesProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    MoviesDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES);

        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", CODE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        throw new RuntimeException("not implemented!");
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {

            case CODE_MOVIES_WITH_ID: {

                String favoriteMovieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{favoriteMovieId};

                retCursor = db.query(TABLE_NAME,
                        projection,
                        COLUMN_MOVIES_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_MOVIES: {

                sortOrder = MoviesContract.MoviesEntry._ID + " ASC";

                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case CODE_MOVIES: {

                        long id = db.insert(TABLE_NAME, null, values);
                        if ( id > 0 ) {
                            returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                        } else {
                            throw new android.database.SQLException("Failed to insert row into " + uri);
                        }

                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int movieDeleted = 0;

        switch (match) {

            case CODE_MOVIES_WITH_ID: {

                String movieID = uri.getPathSegments().get(1);

                movieDeleted = db.delete(TABLE_NAME, "movies_id=?", new String[]{movieID});

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("not implemented");
    }

}
