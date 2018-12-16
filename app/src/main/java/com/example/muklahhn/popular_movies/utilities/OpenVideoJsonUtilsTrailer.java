package com.example.muklahhn.popular_movies.utilities;

import android.content.Context;

import com.example.muklahhn.popular_movies.TrailerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Muklah H N on 16/09/2017.
 */

public final class OpenVideoJsonUtilsTrailer {

    public static ArrayList<TrailerItem> getSimpleVideoStringsFromJson(Context context, String videosJsonString)
            throws JSONException {

        final String RESULTS = "results";
        final String ID = "id";
        final String NAME = "name";
        final String KEY = "key";

        ArrayList<TrailerItem> parsedVideoData = new ArrayList<TrailerItem>();

        JSONObject videosObject = new JSONObject(videosJsonString);
        JSONArray videosArray = videosObject.getJSONArray(RESULTS);

        for (int i = 0; i < videosArray.length(); i++) {
            String id;
            String name;
            String key;

            videosObject = videosArray.getJSONObject(i);

            id = videosObject.getString(ID);
            name = videosObject.getString(NAME);
            key = videosObject.getString(KEY);

            parsedVideoData.add(new TrailerItem(id, name, key));

        }

        return parsedVideoData;
    }
}