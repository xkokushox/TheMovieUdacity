package com.freakybyte.movies.control.movies.impl;

import android.support.v4.app.FragmentActivity;

import com.freakybyte.movies.R;
import com.freakybyte.movies.control.movies.constructor.GridMoviesPresenter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesView;
import com.freakybyte.movies.model.MoviesResponse;
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

    private Call<MoviesResponse> callWebService;
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

        MoviesEndpointInterface apiService = RetrofitBuilder.getRetrofitBuilder().create(MoviesEndpointInterface.class);

        Map<String, String> mapMovies = new HashMap<>();
        mapMovies.put("api_key", mActivity.getString(R.string.the_movie_db_api_key));
        mapMovies.put("page", String.valueOf(page));
        switch (mMovieFilter) {
            case NEW:
                callWebService = apiService.getNewPlayingMovies(mapMovies);
                break;
            case POPULAR:
                callWebService = apiService.getMostPopularMovies(mapMovies);
                break;
            case TOP_RATED:
                callWebService = apiService.getTopRatedMovies(mapMovies);
                break;
            case UPCOMING:
                callWebService = apiService.getUpcomingMovies(mapMovies);
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
                            mView.updateGridMovies(aMovies.getResults());
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
    public void cancelDownload() {
        if (callWebService != null)
            callWebService.cancel();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mActivity = null;
    }
}
