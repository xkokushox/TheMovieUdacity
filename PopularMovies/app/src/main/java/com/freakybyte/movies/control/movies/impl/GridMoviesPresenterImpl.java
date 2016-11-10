package com.freakybyte.movies.control.movies.impl;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.freakybyte.movies.R;
import com.freakybyte.movies.control.movie.MovieDetailActivity;
import com.freakybyte.movies.control.movies.constructor.GridMoviesPresenter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesView;
import com.freakybyte.movies.model.MoviesResponse;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.util.ConstantUtils;
import com.freakybyte.movies.util.DebugUtils;
import com.freakybyte.movies.web.MoviesEndpointInterface;
import com.freakybyte.movies.web.RetrofitBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class GridMoviesPresenterImpl implements GridMoviesPresenter {
    public static final String TAG = GridMoviesPresenterImpl.class.getSimpleName();

    private GridMoviesView mView;
    private FragmentActivity mActivity;

    private ConstantUtils.movieFilter mMovieFilter;

    public GridMoviesPresenterImpl(FragmentActivity activity, GridMoviesView view) {
        mView = view;
        mActivity = activity;
        mMovieFilter = ConstantUtils.movieFilter.POPULAR;
    }

    @Override
    public void getMovies(int page) {
        DebugUtils.logDebug(TAG, "GetItemsFromServer: Start  Page:: " + page);
        mView.showLoader();

        final String subtitle;
        Call<MoviesResponse> callWebService;

        MoviesEndpointInterface apiService = RetrofitBuilder.getRetrofitBuilder().create(MoviesEndpointInterface.class);

        Map<String, String> mapMovies = new HashMap<>();
        mapMovies.put("api_key", mActivity.getString(R.string.the_movie_db_api_key));
        mapMovies.put("page", String.valueOf(page));
        switch (mMovieFilter) {
            case NEW:
                callWebService = apiService.getNewPlayingMovies(mapMovies);
                subtitle = "New";
                break;
            case POPULAR:
                callWebService = apiService.getMostPopularMovies(mapMovies);
                subtitle = "Popular";
                break;
            case TOP_RATED:
                callWebService = apiService.getTopRatedMovies(mapMovies);
                subtitle = "Top Rated";
                break;
            case UPCOMING:
                callWebService = apiService.getUpcomingMovies(mapMovies);
                subtitle = "Upcoming";
                break;
            default:
                callWebService = apiService.getMostPopularMovies(mapMovies);
                subtitle = "";
                break;
        }
        callWebService.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (mView != null) {
                    switch (response.code()) {
                        case 200:
                            MoviesResponse aMovies = response.body();
                            DebugUtils.logDebug(TAG, "GetItemsFromServer: Num Movies:: " + aMovies.getResults().size());
                            mView.updateGridMovies(aMovies.getResults(), subtitle);
                            break;
                        default:
                            DebugUtils.logError("GetItemsFromServer:: Error Code:: " + response.code());
                            break;
                    }

                    mView.closeLoaders();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                DebugUtils.logError("GetItemsFromServer:: onFailure:: " + t.getLocalizedMessage());
                if (mView != null)
                    mView.closeLoaders();
            }

        });
    }

    @Override
    public void setFilterType(ConstantUtils.movieFilter filter) {
        this.mMovieFilter = filter;
    }

    @Override
    public ConstantUtils.movieFilter getFilterType() {
        return mMovieFilter;
    }

    @Override
    public void getMovieDetail(int id) {
        DebugUtils.logDebug(TAG, "GetMovieDetail: Movie:: " + id);
        mView.showLoader();

        MoviesEndpointInterface apiService = RetrofitBuilder.getRetrofitBuilder().create(MoviesEndpointInterface.class);
        Call<MovieResponseModel> callWebService;

        Map<String, String> mapMovies = new HashMap<>();
        mapMovies.put("api_key", mActivity.getString(R.string.the_movie_db_api_key));
        mapMovies.put("language", "es-mx");
        callWebService = apiService.getMovieDetail(id, mapMovies);

        callWebService.enqueue(new Callback<MovieResponseModel>() {
            @Override
            public void onResponse(Call<MovieResponseModel> call, Response<MovieResponseModel> response) {
                if (mView != null) {
                    switch (response.code()) {
                        case 200:
                            MovieResponseModel aMovies = response.body();
                            DebugUtils.logDebug(TAG, "getMovieDetail: Movie:: " + aMovies.getTitle());
                            Intent iMovieDetail = new Intent(mActivity, MovieDetailActivity.class);
                            iMovieDetail.putExtra(MovieResponseModel.TAG, aMovies);
                            mActivity.startActivity(iMovieDetail);
                            break;
                        default:
                            DebugUtils.logError("getMovieDetail:: Error Code:: " + response.code());
                            break;
                    }

                    mView.closeLoaders();
                }
            }

            @Override
            public void onFailure(Call<MovieResponseModel> call, Throwable t) {
                DebugUtils.logError("GetItemsFromServer:: onFailure:: " + t.getLocalizedMessage());
                if (mView != null)
                    mView.closeLoaders();
            }

        });
    }

    @Override
    public void cancelDownload() {
    }

    @Override
    public void onDestroy() {
        mView = null;
        mActivity = null;
    }
}
