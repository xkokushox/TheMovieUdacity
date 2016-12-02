package com.freakybyte.movies.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.freakybyte.movies.data.dao.FavoriteDao;
import com.freakybyte.movies.data.dao.MovieDao;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.utils.PollingCheck;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class TestUtilities extends AndroidTestCase {

    public static final long TEST_FAVORITE_ID = 1l;
    public static final int TEST_MOVIE_ID = 1232;
    public int BULK_INSERT_RECORDS_TO_INSERT = 10;

    public static final MovieResponseModel mTestMovie = new MovieResponseModel("pathBack", TEST_MOVIE_ID, "originalTitle", "overview", "title", "posterPath", "2015-01-01", 1232, 6.9f);


    public ContentValues createMovieValues() {
        return MovieDao.getInstance(mContext).createMovieValue(mTestMovie);
    }

    public ContentValues createFavoriteValues(long movieRowId) {
        return FavoriteDao.getInstance(mContext).createFavoriteValues(movieRowId);
    }

    public void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    public ContentValues[] createBulkInsertMoviesValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        ArrayList<MovieResponseModel> aMovies = getMovieArrayResponse();

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++)
            returnContentValues[i] = MovieDao.getInstance(getContext()).createMovieValue(aMovies.get(i));

        return returnContentValues;
    }

    public ContentValues[] createBulkInsertFavoriteValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        ArrayList<MovieResponseModel> aMovies = getMovieArrayResponse();

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++)
            returnContentValues[i] = FavoriteDao.getInstance(getContext()).createFavoriteValues(aMovies.get(i).getId());

        return returnContentValues;
    }

    public ArrayList<MovieResponseModel> getMovieArrayResponse() {
        int currentMovieId = TEST_MOVIE_ID;
        ArrayList<MovieResponseModel> aMovies = new ArrayList<>();

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            currentMovieId += 1;
            aMovies.add(new MovieResponseModel("pathBack", currentMovieId, "originalTitle", "overview", "title", "posterPath", "2015-01-01", 1232, 6.9f));
        }

        return aMovies;
    }
}
