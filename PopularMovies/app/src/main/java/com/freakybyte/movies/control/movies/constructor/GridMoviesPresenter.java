package com.freakybyte.movies.control.movies.constructor;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public interface GridMoviesPresenter {

    void getMostRecentMovies();

    void getPopularMovies(int page);

    void onDestroy();

    void cancelDownload();
}
