package com.freakybyte.movies.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.freakybyte.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class ReviewItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_movie_review_author)
    public TextView tvMovieReviewAuthor = null;
    @BindView(R.id.tv_movie_review_content)
    public TextView tvMovieReviewContent = null;
    private View mView;


    public ReviewItemHolder(View view) {
        super(view);
        this.mView = view;
        ButterKnife.bind(this, view);
    }

    public TextView getTvMovieReviewAuthor() {
        return tvMovieReviewAuthor;
    }

    public TextView getTvMovieReviewContent() {
        return tvMovieReviewContent;
    }

    public View getView() {
        return mView;
    }
}