package com.freakybyte.movies.control.movie.constructor;

import com.freakybyte.movies.model.review.ReviewMovieModel;

import java.util.ArrayList;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public interface MovieDetailView {

    void updateMovieReview(ArrayList<ReviewMovieModel> aReviews);

    void showLoader();

    void refreshFavButton(boolean isFavorite);

    void closeLoaders();
}
