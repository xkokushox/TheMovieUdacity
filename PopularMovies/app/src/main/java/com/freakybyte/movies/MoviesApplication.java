package com.freakybyte.movies;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class MoviesApplication extends Application {

    private static MoviesApplication singleton;

    public static MoviesApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        Fresco.initialize(this);
    }
}