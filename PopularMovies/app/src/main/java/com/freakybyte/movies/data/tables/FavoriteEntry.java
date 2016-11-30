package com.freakybyte.movies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class FavoriteEntry extends MovieContract implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

    public static final String TABLE_NAME = "favorite_table";

    public static final String COLUMN_MOVIE_KEY = "movie_fk_id";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

            COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +

            " FOREIGN KEY (" + COLUMN_MOVIE_KEY + ") REFERENCES " +
            MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + "), " +

            " UNIQUE (" + COLUMN_MOVIE_KEY + ") ON CONFLICT REPLACE);";


    public static Uri buildFavoriteUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
