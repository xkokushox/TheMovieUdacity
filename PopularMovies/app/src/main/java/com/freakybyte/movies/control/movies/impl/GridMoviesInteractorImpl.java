package com.freakybyte.movies.control.movies.impl;

import com.freakybyte.movies.MoviesApplication;
import com.freakybyte.movies.R;
import com.freakybyte.movies.control.movies.constructor.GridMoviesInteractor;
import com.freakybyte.movies.control.movies.listener.OnRequestMovieDetailListener;
import com.freakybyte.movies.control.movies.listener.OnRequestMoviesListener;
import com.freakybyte.movies.data.dao.FavoriteDao;
import com.freakybyte.movies.data.dao.MovieDao;
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
 * Created by Jose Torres on 01/12/2016.
 */

public class GridMoviesInteractorImpl implements GridMoviesInteractor {
    public static final String TAG = GridMoviesInteractorImpl.class.getSimpleName();

    @Override
    public void getMoviesFromDB(OnRequestMoviesListener listener, int page) {
        if (page == 1)
            listener.onRequestSuccess(FavoriteDao.getInstance().getAllMovies(), "Favorites");
        else
            listener.onRequestFailed();
    }

    @Override
    public void getMoviesFromServer(final OnRequestMoviesListener listener, ConstantUtils.movieFilter filter, int page) {
        Call<MoviesResponse> callWebService;
        final String subtitle;

        MoviesEndpointInterface apiService = RetrofitBuilder.getRetrofitBuilder().create(MoviesEndpointInterface.class);

        Map<String, String> mapMovies = new HashMap<>();
        mapMovies.put("api_key", MoviesApplication.getInstance().getString(R.string.the_movie_db_api_key));
        mapMovies.put("page", String.valueOf(page));
        mapMovies.put("language", "en-US");
        switch (filter) {
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
                if (listener != null) {
                    switch (response.code()) {
                        case 200:
                            DebugUtils.logDebug(TAG, "GetItemsFromServer: Num Movies:: " + response.body().getResults().size());
                            listener.onRequestSuccess(response.body().getResults(), subtitle);
                            break;
                        default:
                            DebugUtils.logError("GetItemsFromServer:: Error Code:: " + response.code());
                            listener.onRequestFailed();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                DebugUtils.logError("GetItemsFromServer:: onFailure:: " + t.getLocalizedMessage());
                if (listener != null)
                    listener.onRequestFailed();
            }

        });
    }

    @Override
    public void getDetailMovie(final OnRequestMovieDetailListener listener, int movieId) {
        MovieResponseModel mMovie = MovieDao.getInstance().getMovieById(movieId);
        if (mMovie != null) {
            listener.onRequestSuccess(mMovie);
        } else {
            MoviesEndpointInterface apiService = RetrofitBuilder.getRetrofitBuilder().create(MoviesEndpointInterface.class);
            Call<MovieResponseModel> callWebService;

            Map<String, String> mapMovies = new HashMap<>();
            mapMovies.put("api_key", MoviesApplication.getInstance().getString(R.string.the_movie_db_api_key));
            mapMovies.put("language", "en-US");
            callWebService = apiService.getMovieDetail(movieId, mapMovies);

            callWebService.enqueue(new Callback<MovieResponseModel>() {
                @Override
                public void onResponse(Call<MovieResponseModel> call, Response<MovieResponseModel> response) {
                    switch (response.code()) {
                        case 200:
                            MovieDao.getInstance().insertMovieByModel(response.body());
                            listener.onRequestSuccess(response.body());
                            break;
                        default:
                            DebugUtils.logError("getMovieDetail:: Error Code:: " + response.code());
                            listener.onRequestFailed();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<MovieResponseModel> call, Throwable t) {
                    DebugUtils.logError("GetItemsFromServer:: onFailure:: " + t.getLocalizedMessage());
                    listener.onRequestFailed();
                }

            });
        }
    }
}
