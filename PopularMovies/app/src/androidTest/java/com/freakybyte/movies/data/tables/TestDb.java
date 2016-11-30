package com.freakybyte.movies.data.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.freakybyte.movies.data.DBAdapter;
import com.freakybyte.movies.data.MovieDbHelper;
import com.freakybyte.movies.data.dao.FavoriteDao;
import com.freakybyte.movies.data.dao.MovieDao;

import java.util.HashSet;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(FavoriteEntry.TABLE_NAME);
        tableNameHashSet.add(MovieEntry.TABLE_NAME);
        tableNameHashSet.add(PopularEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain tables
        assertTrue("Error: Your database was created without some tables",
                tableNameHashSet.isEmpty());


        c = db.rawQuery("PRAGMA table_info(" + FavoriteEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> favoriteColumnHashSet = new HashSet<>();
        favoriteColumnHashSet.add(FavoriteEntry._ID);
        favoriteColumnHashSet.add(FavoriteEntry.COLUMN_MOVIE_KEY);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            favoriteColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The Favorite database doesn't contain all of the required location entry columns",
                favoriteColumnHashSet.isEmpty());


        c = db.rawQuery("PRAGMA table_info(" + MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> movieColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(MovieEntry._ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_POSTER_PATH);
        movieColumnHashSet.add(MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieEntry.COLUMN_ORIGINAL_TITLE);
        movieColumnHashSet.add(MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieEntry.COLUMN_BACKDROP_PATH);
        movieColumnHashSet.add(MovieEntry.COLUMN_VOTE_AVERAGE);
        movieColumnHashSet.add(MovieEntry.COLUMN_VOTE_RUNTIME);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The Movie database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());


        c = db.rawQuery("PRAGMA table_info(" + PopularEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> popularColumnHashSet = new HashSet<>();
        popularColumnHashSet.add(PopularEntry._ID);
        popularColumnHashSet.add(PopularEntry.COLUMN_MOVIE_ID);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            popularColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The Popular database doesn't contain all of the required location entry columns",
                favoriteColumnHashSet.isEmpty());

        db.close();
    }


    public void testMovieTable() {
        insertMovie();
    }

    public long insertMovie() {
        TestUtilities utils = new TestUtilities();

        long movieRowId = MovieDao.getInstance(mContext).insertMovieByContent(utils.createMovieValues());

        assertTrue("Error: Insert Movie in table", movieRowId != MovieDbHelper.FAIL_DB_MODIFY);
        DBAdapter.getInstance(mContext).open();
        Cursor cursor = DBAdapter.getInstance(mContext).getData(MovieEntry.TABLE_NAME);

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from movie query", cursor.moveToFirst());

        utils.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, utils.createMovieValues());

        assertFalse("Error: More than one record returned from location query",
                cursor.moveToNext());

        cursor.close();
        DBAdapter.getInstance(mContext).close();
        return movieRowId;
    }

    public void testFavoriteTable() {
        long movieRowId = insertMovie();

        assertFalse("Error: Movie Not Inserted Correctly", movieRowId == MovieDbHelper.FAIL_DB_MODIFY);

        TestUtilities utils = new TestUtilities();

        ContentValues weatherValues = utils.createFavoriteValues(movieRowId);

        long favoriteRowId = FavoriteDao.getInstance(mContext).insertFavoriteMovie(movieRowId);

        assertTrue("Error: Insert Favorite in table", favoriteRowId != MovieDbHelper.FAIL_DB_MODIFY);

        DBAdapter.getInstance(mContext).open();

        Cursor cursor = DBAdapter.getInstance(mContext).getData(FavoriteEntry.TABLE_NAME);

        assertTrue("Error: No Records returned from favorite query", cursor.moveToFirst());

        utils.validateCurrentRecord("testInsertReadDb favoriteEntry failed to validate",
                cursor, weatherValues);

        assertFalse("Error: More than one record returned from weather query", cursor.moveToNext());

        cursor.close();
        DBAdapter.getInstance(mContext).close();
    }

}
