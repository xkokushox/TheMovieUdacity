package com.freakybyte.movies.data.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.freakybyte.movies.data.DBAdapter;
import com.freakybyte.movies.data.tables.FavoriteEntry;
import com.freakybyte.movies.data.tables.MovieContract;
import com.freakybyte.movies.data.tables.MovieEntry;
import com.freakybyte.movies.util.DebugUtils;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class MovieProvider extends ContentProvider {
    public static final String TAG = "MovieProvider";

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static final int MOVIES = 69;
    public static final int MOVIE_ID = 70;
    public static final int FAVORITES = 71;
    public static final int FAVORITE_ID = 72;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static {
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                FavoriteEntry.TABLE_NAME + " INNER JOIN " +
                        MovieEntry.TABLE_NAME +
                        " ON " + FavoriteEntry.TABLE_NAME +
                        "." + FavoriteEntry.COLUMN_MOVIE_KEY +
                        " = " + MovieEntry.TABLE_NAME +
                        "." + MovieEntry.COLUMN_MOVIE_ID);
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE, FAVORITES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE + "/#", FAVORITE_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                return FavoriteEntry.CONTENT_TYPE;
            case FAVORITE_ID:
                return FavoriteEntry.CONTENT_ITEM_TYPE;
            case MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        SQLiteQueryBuilder queryBuilder;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                retCursor = DBAdapter.getInstance(getContext()).query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            case MOVIE_ID:
                queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(MovieEntry.TABLE_NAME);
                queryBuilder.appendWhere(MovieEntry.COLUMN_MOVIE_ID + "=" + uri.getLastPathSegment());
                retCursor = DBAdapter.getInstance(getContext()).query(queryBuilder,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            case FAVORITES:
                retCursor = DBAdapter.getInstance(getContext()).query(sWeatherByLocationSettingQueryBuilder,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            case FAVORITE_ID:
                queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(FavoriteEntry.TABLE_NAME);
                queryBuilder.appendWhere(FavoriteEntry.COLUMN_MOVIE_KEY + "=" + uri.getLastPathSegment());
                retCursor = DBAdapter.getInstance(getContext()).query(queryBuilder,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = DBAdapter.getInstance(getContext()).insert(MovieEntry.TABLE_NAME, values);
                if (_id > 0)
                    returnUri = MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITES:
                long _id = DBAdapter.getInstance(getContext()).insert(FavoriteEntry.TABLE_NAME, values);
                if (_id > 0)
                    returnUri = FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE_ID:
                rowsDeleted = DBAdapter.getInstance(getContext()).delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_ID:
                rowsDeleted = DBAdapter.getInstance(getContext()).delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES:
                rowsDeleted = DBAdapter.getInstance(getContext()).deleteAll(MovieEntry.TABLE_NAME);
                break;
            case FAVORITES:
                rowsDeleted = DBAdapter.getInstance(getContext()).deleteAll(FavoriteEntry.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        DebugUtils.logDebug(TAG, "Uri:: " + uri.getPath() + " Records Deleted::" + rowsDeleted);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE_ID:
                rowsUpdated = DBAdapter.getInstance().update(MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITES:
                rowsUpdated = DBAdapter.getInstance().update(FavoriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        DebugUtils.logDebug(TAG, "Uri:: " + uri.getPath() + " Records Updated::" + rowsUpdated);
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                int returnCount = DBAdapter.getInstance(getContext()).insertBulk(MovieEntry.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        super.shutdown();
    }

}