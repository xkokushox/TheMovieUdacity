package com.freakybyte.movies.control.movie;

import android.os.Bundle;
import android.util.Log;

import com.freakybyte.movies.R;
import com.freakybyte.movies.control.BaseActivity;
import com.freakybyte.movies.model.movie.MovieResponseModel;

public class MovieDetailActivity extends BaseActivity {
    public static final String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MovieResponseModel mMovie = getIntent().getParcelableExtra(MovieResponseModel.TAG);

        Log.d(TAG, "Movie:: " + mMovie.getTitle());
    }
}
