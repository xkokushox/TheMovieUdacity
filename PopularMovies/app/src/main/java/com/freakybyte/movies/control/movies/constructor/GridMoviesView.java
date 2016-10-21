package com.freakybyte.movies.control.movies.constructor;

import com.freakybyte.movies.model.ResultModel;

import java.util.List;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public interface GridMoviesView {

    void updateGridMovies(List<ResultModel> aGallery);

    void showLoader();

    void closeLoaders();
}
