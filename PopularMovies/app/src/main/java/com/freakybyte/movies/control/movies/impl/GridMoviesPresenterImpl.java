package com.freakybyte.movies.control.movies.impl;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.freakybyte.movies.control.movie.ui.MovieDetailActivity;
import com.freakybyte.movies.control.movies.constructor.GridMoviesInteractor;
import com.freakybyte.movies.control.movies.constructor.GridMoviesPresenter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesView;
import com.freakybyte.movies.control.movies.listener.OnRequestMovieDetailListener;
import com.freakybyte.movies.control.movies.listener.OnRequestMoviesListener;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.util.ConstantUtils;
import com.freakybyte.movies.util.DebugUtils;

import java.util.ArrayList;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class GridMoviesPresenterImpl implements GridMoviesPresenter, OnRequestMoviesListener {
    public static final String TAG = GridMoviesPresenterImpl.class.getSimpleName();

    private GridMoviesView mView;
    private GridMoviesInteractor mInteractor;
    private FragmentActivity mActivity;

    private ConstantUtils.movieFilter mMovieFilter;

    public GridMoviesPresenterImpl(FragmentActivity activity, GridMoviesView view) {
        mView = view;
        mInteractor = new GridMoviesInteractorImpl();
        mActivity = activity;
        mMovieFilter = ConstantUtils.movieFilter.POPULAR;
    }

    @Override
    public void getMovies(int page) {
        DebugUtils.logDebug(TAG, "GetItemsFromServer: Start  Page:: " + page);
        mView.showLoader();

        switch (mMovieFilter) {
            case FAVORITE:
                mInteractor.getMoviesFromDB(this, page);
                break;
            default:
                mInteractor.getMoviesFromServer(this, mMovieFilter, page);
                break;
        }
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
    public void getMovieDetail(int movieId) {
        DebugUtils.logDebug(TAG, "GetMovieDetail: Movie:: " + movieId);
        mView.showLoader();

        mInteractor.getDetailMovie(new OnRequestMovieDetailListener() {
            @Override
            public void onRequestFailed() {
                if (mView != null)
                    mView.closeLoaders();
            }

            @Override
            public void onRequestSuccess(MovieResponseModel movie) {
                if (mView != null) {
                    mView.closeLoaders();
                    Intent iMovieDetail = new Intent(mActivity, MovieDetailActivity.class);
                    iMovieDetail.putExtra(MovieResponseModel.TAG, movie);
                    mActivity.startActivity(iMovieDetail);
                }
            }
        }, movieId);

    }

    @Override
    public void cancelDownload() {
    }

    @Override
    public void onDestroy() {
        mView = null;
        mActivity = null;
    }

    @Override
    public void onRequestFailed() {
        if (mView != null)
            mView.closeLoaders();
    }

    @Override
    public void onRequestSuccess(ArrayList<MovieResponseModel> aMovies, String subtitle) {
        if (mView != null) {
            mView.closeLoaders();
            mView.updateGridMovies(aMovies, subtitle);
        }
    }
}
