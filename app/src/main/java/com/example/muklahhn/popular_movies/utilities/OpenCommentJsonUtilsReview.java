package com.example.muklahhn.popular_movies.utilities;

import android.content.Context;

import com.example.muklahhn.popular_movies.ReviewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Muklah H N on 16/09/2017.
 */

public final class OpenCommentJsonUtilsReview {

    public static ArrayList<ReviewItem> getSimpleCommentStringsFromJson(Context context, String commentsJsonString)
            throws JSONException {

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        ArrayList<ReviewItem> parsedCommentData = new ArrayList<ReviewItem>();

        JSONObject commentsObject = new JSONObject(commentsJsonString);
        JSONArray commentsArray = commentsObject.getJSONArray(RESULTS);

        for (int i = 0; i < commentsArray.length(); i++) {
            String id;
            String author;
            String content;

            commentsObject = commentsArray.getJSONObject(i);

            author = commentsObject.getString(AUTHOR);
            content = commentsObject.getString(CONTENT);

            parsedCommentData.add(new ReviewItem(author, content));

        }

        return parsedCommentData;
    }
}