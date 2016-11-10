package com.freakybyte.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.freakybyte.movies.data.tables.GenreEntry;
import com.freakybyte.movies.data.tables.MovieEntry;
import com.freakybyte.movies.data.tables.PopularEntry;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final long FAIL_DB_MODIFY = -1L;

    static final String DATABASE_NAME = "udacity_movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(GenreEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(PopularEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GenreEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
