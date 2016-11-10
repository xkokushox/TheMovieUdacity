package com.freakybyte.movies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class PopularEntry extends MovieContract implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

    public static final String TABLE_NAME = "popular_table";

    public static final String COLUMN_MOVIE_ID = "movie_id";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

            COLUMN_MOVIE_ID + " INT UNIQUE NOT NULL);";


    public static Uri buildPopularUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

}
