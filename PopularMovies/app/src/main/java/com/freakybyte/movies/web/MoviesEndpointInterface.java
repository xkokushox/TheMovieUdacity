package com.freakybyte.movies.web;

import com.freakybyte.movies.model.MoviesResponse;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.model.review.ReviewMovieResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public interface MoviesEndpointInterface {

    @GET("popular?")
    Call<MoviesResponse> getMostPopularMovies(@QueryMap Map<String, String> options);

    @GET("now_playing?")
    Call<MoviesResponse> getNewPlayingMovies(@QueryMap Map<String, String> options);

    @GET("top_rated?")
    Call<MoviesResponse> getTopRatedMovies(@QueryMap Map<String, String> options);

    @GET("upcoming?")
    Call<MoviesResponse> getUpcomingMovies(@QueryMap Map<String, String> options);

    @GET("{id}?")
    Call<MovieResponseModel> getMovieDetail(@Path("id") int groupId, @QueryMap Map<String, String> options);

    @GET("{movie_id}/reviews?")
    Call<ReviewMovieResult> getMovieReviews(@Path("movie_id") int movieId, @QueryMap Map<String, String> options);

}
