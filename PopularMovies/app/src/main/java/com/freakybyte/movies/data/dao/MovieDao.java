package com.freakybyte.movies.data.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.freakybyte.movies.data.DBAdapter;
import com.freakybyte.movies.data.tables.FavoriteEntry;
import com.freakybyte.movies.data.tables.MovieEntry;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.util.DebugUtils;

import java.util.ArrayList;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class MovieDao {

    private static final String TAG = MovieDao.class.getSimpleName();

    private static MovieDao singleton;

    private static Context mContext;

    public static MovieDao getInstance() {
        if (singleton == null)
            singleton = new MovieDao();
        return singleton;
    }

    public static MovieDao getInstance(Context context) {
        if (singleton == null)
            singleton = new MovieDao(context);
        return singleton;
    }

    public MovieDao() {
    }

    public MovieDao(Context context) {
        this.mContext = context;
    }

    public ContentValues createMovieValue(MovieResponseModel mMovie) {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
        testValues.put(MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
        testValues.put(MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
        testValues.put(MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        testValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());
        testValues.put(MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        testValues.put(MovieEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
        testValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
        testValues.put(MovieEntry.COLUMN_VOTE_RUNTIME, mMovie.getRuntime());

        return testValues;
    }

    public long insertMovieByContent(ContentValues contentValues) {
        return ContentUris.parseId(insertContentMovie(contentValues));
    }

    public Uri insertContentMovie(ContentValues contentValues) {
        return mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
    }

    public void insertMovieByModel(MovieResponseModel mMovie) {
        ContentValues contentValue = createMovieValue(mMovie);
        insertContentMovie(contentValue);
    }

    public int insertMovies(ArrayList<MovieResponseModel> aMovies) {
        ContentValues[] bulkInsertContentValues = new ContentValues[aMovies.size()];
        for (int i = 0; i < aMovies.size(); i++)
            bulkInsertContentValues[i] = createMovieValue(aMovies.get(i));

        return insertBulkMovies(bulkInsertContentValues);
    }

    public int insertBulkMovies(ContentValues[] bulkInsertContentValues) {
        int numInserted = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, bulkInsertContentValues);
        DebugUtils.logDebug(TAG, "Movies Inserted:: " + numInserted);
        return numInserted;
    }

    public static int deleteAll() {
        return mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );
    }

    public static int deleteMovie(int idMovie) {
        String mSelectionClause = MovieEntry.COLUMN_MOVIE_ID + " = ?";
        String[] mSelectionArgs = {String.valueOf(idMovie)};
        return mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                mSelectionClause,
                mSelectionArgs
        );
    }

}