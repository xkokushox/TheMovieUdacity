package com.freakybyte.movies.control.movies.constructor;

import com.freakybyte.movies.control.movies.listener.OnRequestMovieDetailListener;
import com.freakybyte.movies.control.movies.listener.OnRequestMoviesListener;
import com.freakybyte.movies.util.ConstantUtils;

/**
 * Created by Jose Torres on 01/12/2016.
 */

public interface GridMoviesInteractor {

    void getMoviesFromDB(OnRequestMoviesListener listener, int page);

    void getMoviesFromServer(OnRequestMoviesListener listener, ConstantUtils.movieFilter filter, int page);

    void getDetailMovie(OnRequestMovieDetailListener listener, int movieId);
}
