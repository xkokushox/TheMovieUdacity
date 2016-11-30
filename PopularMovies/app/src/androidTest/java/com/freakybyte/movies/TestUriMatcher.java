package com.freakybyte.movies;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.freakybyte.movies.data.provider.MovieProvider;
import com.freakybyte.movies.data.tables.FavoriteEntry;
import com.freakybyte.movies.data.tables.MovieEntry;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class TestUriMatcher extends AndroidTestCase {

    private static final Uri TEST_MOVIE_DIR = MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_ID_DIR = MovieEntry.buildMovieUri(123);
    private static final Uri TEST_FAVORITE_DIR = FavoriteEntry.CONTENT_URI;


    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIES);
        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_FAVORITE_DIR), MovieProvider.FAVORITES);
        assertEquals("Error: The MOVIE WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_ID_DIR), MovieProvider.MOVIE_ID);
    }
}