package com.freakybyte.movies.data.tables;

import android.net.Uri;

/**
 * Created by Jose Torres on 10/11/2016.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.freakybyte.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_POPULAR = "popular_movies";
    public static final String PATH_FAVORITE = "favorite_movies";
}
