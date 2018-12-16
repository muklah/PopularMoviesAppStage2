/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.muklahhn.popular_movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {

public static final String DATABASE_NAME = "movies.db";

private static final int DATABASE_VERSION = 5;

public MoviesDbHelper(Context context) {

    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +

                        MoviesContract.MoviesEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesContract.MoviesEntry.COLUMN_MOVIES_ID + " INTEGER NOT NULL, "                 +
                        MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "                 +
                        MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " BLOB NOT NULL, "                 +
                        MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "                 +
                        MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "                 +
                        MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE    + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}