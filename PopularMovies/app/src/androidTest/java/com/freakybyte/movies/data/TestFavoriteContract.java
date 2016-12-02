package com.freakybyte.movies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.freakybyte.movies.data.tables.FavoriteEntry;

import org.junit.Test;

/**
 * Created by Jose Torres on 01/12/2016.
 */

public class TestFavoriteContract extends AndroidTestCase {

    private static final long TEST_MOVIE_ID = 271110;

    @Test
    public void testBuildMovieId() throws Exception {
        Uri movieUri = FavoriteEntry.buildFavoriteUri(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                "FavoriteContract.", movieUri);
        assertEquals("Error: Favorite id not properly appended to the end of the Uri",
                String.valueOf(TEST_MOVIE_ID), movieUri.getLastPathSegment());
        assertEquals("Error: Favorite id Uri doesn't match our expected result",
                movieUri.toString(),
                "content://com.freakybyte.movies/favorite_movies/" + TEST_MOVIE_ID);
    }
}
