package com.freakybyte.movies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class MovieEntry extends MovieContract implements BaseColumns {

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    public static final String TABLE_NAME = "movie_table";

    public static final String COLUMN_MOVIE_ID = "id";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_ADULT = "adult";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

            COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            COLUMN_POSTER_PATH + "  TEXT NOT NULL, " +
            COLUMN_POSTER_PATH + "  TEXT NOT NULL, " +
            COLUMN_ADULT + " INTEGER NOT NULL, " +
            COLUMN_OVERVIEW + "  TEXT NOT NULL, " +
            COLUMN_RELEASE_DATE + "  TEXT NOT NULL, " +
            COLUMN_ORIGINAL_TITLE + "  TEXT NOT NULL, " +
            COLUMN_TITLE + "  TEXT NOT NULL, " +
            COLUMN_BACKDROP_PATH + "  TEXT NOT NULL, " +
            COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
            " );";


    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }


}