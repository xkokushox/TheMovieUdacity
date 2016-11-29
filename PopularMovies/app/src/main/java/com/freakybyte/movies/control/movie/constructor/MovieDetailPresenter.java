package com.freakybyte.movies.control.movie.constructor;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public interface MovieDetailPresenter {

    void getMovieReviews(int id, int page);

    void onDestroy();
}
