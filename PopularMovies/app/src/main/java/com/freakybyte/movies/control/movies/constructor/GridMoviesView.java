package com.freakybyte.movies.control.movies.constructor;

import com.freakybyte.movies.model.movie.MovieResponseModel;

import java.util.ArrayList;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public interface GridMoviesView {

    void updateGridMovies(ArrayList<MovieResponseModel> aGallery, String subtitle);

    void showLoader();

    void closeLoaders();
}
