package com.freakybyte.movies.control.movies.constructor;


/**
 * Created by Jose Torres on 20/10/2016.
 */

public interface GridMoviesPresenter {

    void getMovies(int page);

    void setFilterType(int filter);

    int getFilterType();

    void getMovieDetail(int id);

    void onDestroy();

    void cancelDownload();
}
