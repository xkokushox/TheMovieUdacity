package com.freakybyte.movies.web;

import com.freakybyte.movies.MoviesApplication;
import com.freakybyte.movies.R;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class RetrofitBuilder {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitBuilder() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(MoviesApplication.getInstance().getString(R.string.url_base))
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        return retrofit;
    }
}
