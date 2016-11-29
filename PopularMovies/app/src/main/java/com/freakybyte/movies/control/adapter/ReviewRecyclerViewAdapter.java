package com.freakybyte.movies.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freakybyte.movies.R;
import com.freakybyte.movies.listener.RecyclerViewListener;
import com.freakybyte.movies.model.review.ReviewMovieModel;
import com.freakybyte.movies.util.DebugUtils;

import java.util.ArrayList;

/**
 * Created by Jose Torres on 29/11/2016.
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewItemHolder> {
    private ArrayList<ReviewMovieModel> aReviews;
    private int iListIndex;

    public ReviewRecyclerViewAdapter() {
        this.aReviews = new ArrayList<>();
        iListIndex = 0;
    }

    @Override
    public ReviewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, null);
        ReviewItemHolder rcv = new ReviewItemHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ReviewItemHolder viewHolder, int position) {
        final ReviewMovieModel mReview = aReviews.get(position);
        viewHolder.getTvMovieReviewAuthor().setText(mReview.getAuthor());
        viewHolder.getTvMovieReviewContent().setText(mReview.getContent());

        iListIndex = position;
    }

    @Override
    public int getItemCount() {
        return this.aReviews.size();
    }

    public ArrayList<ReviewMovieModel> getReviews() {
        return aReviews;
    }

    public int getListIndex() {
        return iListIndex;
    }

    public void swapItems(ArrayList<ReviewMovieModel> aImages) {
        aReviews.clear();
        aReviews.addAll(aImages);
        DebugUtils.logDebug("TotalItems:: ", aReviews.size());
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return aReviews.isEmpty();
    }
}