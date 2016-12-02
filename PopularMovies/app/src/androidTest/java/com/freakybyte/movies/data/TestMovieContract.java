package com.freakybyte.movies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.freakybyte.movies.data.tables.MovieEntry;

import org.junit.Test;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class TestMovieContract extends AndroidTestCase {

    private static final long TEST_MOVIE_ID = 271110;

    @Test
    public void testBuildMovieId() throws Exception {
        Uri movieUri = MovieEntry.buildMovieUri(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                "WeatherContract.", movieUri);
        assertEquals("Error: Movie id not properly appended to the end of the Uri",
                String.valueOf(TEST_MOVIE_ID), movieUri.getLastPathSegment());
        assertEquals("Error: Movie id Uri doesn't match our expected result",
                movieUri.toString(),
                "content://com.freakybyte.movies/movie/" + TEST_MOVIE_ID);
    }

}