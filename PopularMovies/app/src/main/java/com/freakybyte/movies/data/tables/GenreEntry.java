package com.freakybyte.movies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class GenreEntry extends MovieContract implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;

    public static final String TABLE_NAME = "genre_table";

    public static final String COLUMN_GENRE_ID = "genre_id";
    public static final String COLUMN_NAME = "name";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

            COLUMN_GENRE_ID + " INT UNIQUE NOT NULL, " +
            COLUMN_NAME + "  TEXT NOT NULL " +
            ");";


    public static Uri buildGenreUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

}
