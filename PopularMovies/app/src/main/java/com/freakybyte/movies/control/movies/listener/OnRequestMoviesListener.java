package com.freakybyte.movies.control.movies.listener;

import com.freakybyte.movies.model.movie.MovieResponseModel;

import java.util.ArrayList;

/**
 * Created by Jose Torres on 01/12/2016.
 */

public interface OnRequestMoviesListener {

    void onRequestFailed();

    void onRequestSuccess(ArrayList<MovieResponseModel> aMovies, String title);
}
