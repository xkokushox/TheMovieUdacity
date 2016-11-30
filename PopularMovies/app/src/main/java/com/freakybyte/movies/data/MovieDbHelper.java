package com.freakybyte.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.freakybyte.movies.data.tables.FavoriteEntry;
import com.freakybyte.movies.data.tables.MovieEntry;
import com.freakybyte.movies.data.tables.PopularEntry;
import com.freakybyte.movies.util.DebugUtils;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String TAG = "MovieDbHelper";

    private static final int DATABASE_VERSION = 2;
    public static final long FAIL_DB_MODIFY = -1L;

    public static final String DATABASE_NAME = "udacity_pop_movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        DebugUtils.logDebug(TAG, "OnCreate DB");
        sqLiteDatabase.execSQL(MovieEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(PopularEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(FavoriteEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        DebugUtils.logDebug(TAG, "OnUpdate DB");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
