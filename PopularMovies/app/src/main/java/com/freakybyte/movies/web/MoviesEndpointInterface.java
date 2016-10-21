package com.freakybyte.movies.web;

import com.freakybyte.movies.model.MoviesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public interface MoviesEndpointInterface {

    @GET("popular?")
    Call<MoviesResponse> getMostPopularMovies(@QueryMap Map<String, String> options);
}
