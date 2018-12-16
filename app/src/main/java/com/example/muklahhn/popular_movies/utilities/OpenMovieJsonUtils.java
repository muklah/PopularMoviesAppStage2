package com.example.muklahhn.popular_movies.utilities;

import android.content.Context;

import com.example.muklahhn.popular_movies.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Muklah H N on 16/09/2017.
 */

public final class OpenMovieJsonUtils {

    public static ArrayList<MovieItem> getSimpleMovieStringsFromJson(Context context, String moviesJsonString)
            throws JSONException {

        final String RESULTS = "results";
        final String POPULARITY = "popularity";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";
        final String OVERVIEW = "overview";
        final String ID = "id";

        ArrayList<MovieItem> parsedMovieData = new ArrayList<MovieItem>();

        JSONObject moviesObject = new JSONObject(moviesJsonString);
        JSONArray moviesArray = moviesObject.getJSONArray(RESULTS);

        for (int i = 0; i < moviesArray.length(); i++) {
            double popularity;
            String poster_path;
            String original_title;
            String release_date;
            double vote_average;
            String overview;
            int id;

            moviesObject = moviesArray.getJSONObject(i);

            popularity = moviesObject.getDouble(POPULARITY);
            poster_path = moviesObject.getString(POSTER_PATH);
            original_title = moviesObject.getString(ORIGINAL_TITLE);
            release_date = moviesObject.getString(RELEASE_DATE);
            vote_average = moviesObject.getDouble(VOTE_AVERAGE);
            overview = moviesObject.getString(OVERVIEW);
            id = moviesObject.getInt(ID);

            parsedMovieData.add(new MovieItem(popularity, poster_path, original_title, release_date, vote_average, overview, id));

        }

        return parsedMovieData;
    }
}