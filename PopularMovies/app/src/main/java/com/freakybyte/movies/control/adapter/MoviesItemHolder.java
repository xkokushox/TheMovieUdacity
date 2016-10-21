package com.freakybyte.movies.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    private View mView;


    public MoviesItemHolder(View view) {
        super(view);
        this.mView = view;
        ButterKnife.bind(this, view);
    }

    public SimpleDraweeView getImagePoster() {
        return imagePoster;
    }

    public View getView() {
        return mView;
    }
}