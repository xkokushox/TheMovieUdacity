package com.freakybyte.movies.control.movie.impl;

import android.support.v4.app.FragmentActivity;

import com.freakybyte.movies.R;
import com.freakybyte.movies.control.movie.constructor.MovieDetailPresenter;
import com.freakybyte.movies.control.movie.constructor.MovieDetailView;
import com.freakybyte.movies.control.movies.impl.GridMoviesPresenterImpl;
import com.freakybyte.movies.model.review.ReviewMovieResult;
import com.freakybyte.movies.util.DebugUtils;
import com.freakybyte.movies.web.MoviesEndpointInterface;
import com.freakybyte.movies.web.RetrofitBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class MovieDetailPresenterImpl implements MovieDetailPresenter {
    public static final String TAG = GridMoviesPresenterImpl.class.getSimpleName();

    private MovieDetailView mView;
    private FragmentActivity mActivity;

    public MovieDetailPresenterImpl(FragmentActivity activity, MovieDetailView view) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void getMovieReviews(int id, int page) {
        DebugUtils.logDebug(TAG, "Get Reviews from Movies: Start  Page:: " + page);
        mView.showLoader();

        Call<ReviewMovieResult> callWebService;

        MoviesEndpointInterface apiService = RetrofitBuilder.getRetrofitBuilder().create(MoviesEndpointInterface.class);

        Map<String, String> mapMovies = new HashMap<>();
        mapMovies.put("api_key", mActivity.getString(R.string.the_movie_db_api_key));
        mapMovies.put("page", String.valueOf(page));
        mapMovies.put("language", "en-US");
        callWebService = apiService.getMovieReviews(id, mapMovies);
        DebugUtils.logDebug(TAG, "Url:: " + callWebService.request().url());
        callWebService.enqueue(new Callback<ReviewMovieResult>() {
            @Override
            public void onResponse(Call<ReviewMovieResult> call, Response<ReviewMovieResult> response) {
                if (mView != null) {
                    switch (response.code()) {
                        case 200:
                            ReviewMovieResult aReviews = response.body();
                            DebugUtils.logDebug(TAG, "GetMovieReviews: Num Reviews:: " + aReviews.getResults().size());
                            mView.updateMovieReview(aReviews.getResults());
                            break;
                        default:
                            DebugUtils.logError("GetMovieReviews:: Error Code:: " + response.code());
                            break;
                    }

                    mView.closeLoaders();
                }
            }

            @Override
            public void onFailure(Call<ReviewMovieResult> call, Throwable t) {
                DebugUtils.logError("GetMovieReviews:: onFailure:: " + t.getLocalizedMessage());
                if (mView != null)
                    mView.closeLoaders();
            }

        });
    }

    @Override
    public void onDestroy() {
        mView = null;
        mActivity = null;
    }
}
