package com.freakybyte.movies.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.freakybyte.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class MoviesItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.drawee_poster)
    public SimpleDraweeView imagePoster = null;
    @BindView(R.id.wrapper_movie_resume)
    public RelativeLayout wrapperMovieResume = null;
    @BindView(R.id.tv_movie_title)
    public TextView tvMovieTitle = null;
    @BindView(R.id.ib_movie_favorite)
    public ImageButton ibMovieFavorite = null;

    private View mView;


    public MoviesItemHolder(View view) {
        super(view);
        this.mView = view;
        ButterKnife.bind(this, view);
    }

    public SimpleDraweeView getImagePoster() {
        return imagePoster;
    }

    public RelativeLayout getWrapperMovieResume() {
        return wrapperMovieResume;
    }

    public TextView getTvMovieTitle() {
        return tvMovieTitle;
    }

    public ImageButton getIbMovieFavorite() {
        return ibMovieFavorite;
    }

    public View getView() {
        return mView;
    }
}