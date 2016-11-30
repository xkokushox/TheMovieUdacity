package com.freakybyte.movies.data.dao;

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

public class FavoriteDao {

    private static final String TAG = FavoriteDao.class.getSimpleName();

    private static FavoriteDao singleton;

    private static Context mContext;

    public static FavoriteDao getInstance() {
        if (singleton == null)
            singleton = new FavoriteDao();
        return singleton;
    }

    public static FavoriteDao getInstance(Context context) {
        if (singleton == null)
            singleton = new FavoriteDao(context);
        return singleton;
    }

    public FavoriteDao() {
    }

    public FavoriteDao(Context context) {
        this.mContext = context;
    }

    public ContentValues createFavoriteValues(long movieRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(FavoriteEntry.COLUMN_MOVIE_KEY, movieRowId);

        return weatherValues;
    }

    public long insertFavoriteMovie(long idMovie) {
        return DBAdapter.getInstance(mContext).insert(FavoriteEntry.TABLE_NAME, createFavoriteValues(idMovie));
    }

    public long insertFavoriteMovie(ContentValues contentValues) {
        return DBAdapter.getInstance(mContext).insert(FavoriteEntry.TABLE_NAME, contentValues);
    }

    public Uri insertContentFavoriteMovie(ContentValues contentValues) {
        return mContext.getContentResolver().insert(FavoriteEntry.CONTENT_URI, contentValues);
    }

    public int insertFavoriteMovies(ArrayList<MovieResponseModel> aMovies) {
        MovieDao.getInstance(mContext).insertMovies(aMovies);
        ContentValues[] bulkInsertContentValues = new ContentValues[aMovies.size()];
        for (int i = 0; i < aMovies.size(); i++)
            bulkInsertContentValues[i] = createFavoriteValues(aMovies.get(i).getId());

        return insertBulkMovies(bulkInsertContentValues);
    }

    public void insertFavoriteMovie(MovieResponseModel mMovie) {
        MovieDao.getInstance(mContext).insertMovieByModel(mMovie);
        insertContentFavoriteMovie(createFavoriteValues(mMovie.getId()));
    }

    public int insertBulkMovies(ContentValues[] bulkInsertContentValues) {
        int numInserted = mContext.getContentResolver().bulkInsert(FavoriteEntry.CONTENT_URI, bulkInsertContentValues);
        DebugUtils.logDebug(TAG, "Favorites Inserted:: " + numInserted);
        return numInserted;
    }

    public void deleteAll() {
        mContext.getContentResolver().delete(
                FavoriteEntry.CONTENT_URI,
                null,
                null
        );
    }

    public static int deleteFavorite(int idMovie) {
        String mSelectionClause = FavoriteEntry.COLUMN_MOVIE_KEY + " = ?";
        String[] mSelectionArgs = {String.valueOf(idMovie)};
        return mContext.getContentResolver().delete(
                FavoriteEntry.CONTENT_URI,
                mSelectionClause,
                mSelectionArgs
        );
    }
}
