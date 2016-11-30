package com.freakybyte.movies.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.freakybyte.movies.data.DBAdapter;
import com.freakybyte.movies.data.dao.FavoriteDao;
import com.freakybyte.movies.data.dao.MovieDao;
import com.freakybyte.movies.data.tables.FavoriteEntry;
import com.freakybyte.movies.data.tables.MovieEntry;
import com.freakybyte.movies.data.tables.TestUtilities;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {

        MovieDao.getInstance(mContext).deleteAll();
        FavoriteDao.getInstance(mContext).deleteAll();

        Cursor cursor = DBAdapter.getInstance(getContext()).getData(MovieEntry.CONTENT_URI);
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = DBAdapter.getInstance(getContext()).getData(FavoriteEntry.CONTENT_URI);

        assertEquals("Error: Records not deleted from Location table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testContentProvider() {
        actionInsertReadProvider();
        actionDeleteRecords();
        actionBulkMovieInsert();
    }


    public void testUpdateLocation() {

    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(FavoriteEntry.CONTENT_URI);
        assertEquals("Error: the FavoriteEntry CONTENT_URI should return FavoriteEntry.CONTENT_TYPE",
                FavoriteEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        int movieId = 94074;
        type = mContext.getContentResolver().getType(
                MovieEntry.buildMovieUri(movieId));
        assertEquals("Error: the MovieEntry CONTENT_URI with id should return MovieEntry.CONTENT_ITEM_TYPE",
                MovieEntry.CONTENT_ITEM_TYPE, type);

    }

    public void actionInsertReadProvider() {
        TestUtilities utilities = new TestUtilities();
        ContentValues testValues = utilities.createMovieValues();

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        assertTrue(locationRowId != -1);

        Cursor cursorMovie = DBAdapter.getInstance(getContext()).getData(MovieEntry.CONTENT_URI);

        utilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursorMovie, testValues);

        cursorMovie = DBAdapter.getInstance(getContext()).getData(MovieEntry.buildMovieUri(utilities.TEST_MOVIE_ID));

        utilities.validateCursor("testInsertReadProvider. Error validating MovieEntryId.",
                cursorMovie, testValues);

        ContentValues favoriteValues = utilities.createFavoriteValues(utilities.TEST_MOVIE_ID);
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(FavoriteEntry.CONTENT_URI, true, tco);

        Uri favoriteInsertUri = mContext.getContentResolver().insert(FavoriteEntry.CONTENT_URI, favoriteValues);

        assertTrue(favoriteInsertUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        favoriteValues.putAll(testValues);

        Cursor favoriteCursor = DBAdapter.getInstance(mContext).getData(FavoriteEntry.CONTENT_URI);

        utilities.validateCursor("testInsertReadProvider.  Error validating joined Favorite and Movie Data.",
                favoriteCursor, favoriteValues);

    }

    public void actionDeleteRecords() {
        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteEntry.CONTENT_URI, true, locationObserver);

        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, weatherObserver);

        deleteAllRecordsFromProvider();

        locationObserver.waitForNotificationOrFail();
        weatherObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(locationObserver);
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
    }


    public void actionBulkMovieInsert() {
        TestUtilities utils = new TestUtilities();

        ContentValues[] bulkInsertContentValues = utils.createBulkInsertFavoriteValues();

        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteEntry.CONTENT_URI, true, weatherObserver);

        int insertCount = FavoriteDao.getInstance(getContext()).insertFavoriteMovies(utils.getMovieArrayResponse());

        weatherObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);

        assertEquals(insertCount, utils.BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = mContext.getContentResolver().query(
                FavoriteEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                FavoriteEntry._ID + " ASC"  // sort order == by DATE ASCENDING
        );

        assertEquals(utils.BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());

        cursor.moveToFirst();
        for (int i = 0; i < utils.BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            utils.validateCurrentRecord("testBulkInsert.  Error validating Favorites " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}