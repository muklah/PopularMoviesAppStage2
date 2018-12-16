package com.example.muklahhn.popular_movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Muklah H N on 16/09/2017.
 */

public final class NetworkUtilsTrailer {

    private static final String TAG = NetworkUtilsTrailer.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIES_URL  = MOVIES_BASE_URL;
    private static final String VIDEOS_TRAILER  = "videos";
    private static final String apiKey = "36666cbb5d7e20041e705d1b2c4e7a79";

    final static String API_PARAM = "api_key";

    public static URL buildUrl(String VideosQuery) {
        Uri builtUri = Uri.parse(MOVIES_URL).buildUpon()
                .appendPath(VideosQuery)
                .appendPath(VIDEOS_TRAILER)
                .appendQueryParameter(API_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}