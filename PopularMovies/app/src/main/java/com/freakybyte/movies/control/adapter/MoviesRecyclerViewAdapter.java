package com.freakybyte.movies.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freakybyte.movies.R;
import com.freakybyte.movies.listener.RecyclerViewListener;
import com.freakybyte.movies.model.ResultModel;
import com.freakybyte.movies.util.DebugUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jose Torres on 20/10/2016.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesItemHolder> {
    private ArrayList<ResultModel> aGallery;
    private RecyclerViewListener mListener;
    private boolean sendLastItemVisible;
    private int iListIndex;

    public MoviesRecyclerViewAdapter(RecyclerViewListener mListener) {
        this.aGallery = new ArrayList<>();
        this.mListener = mListener;
        iListIndex = 0;
    }

    @Override
    public MoviesItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, null);
        MoviesItemHolder rcv = new MoviesItemHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MoviesItemHolder viewHolder, int position) {
        final ResultModel mImage = aGallery.get(position);

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mImage);
            }
        });

        viewHolder.getImagePoster().setImageURI("https://image.tmdb.org/t/p/w500/" + mImage.getPosterPath());

        if (position == aGallery.size() - 1 && sendLastItemVisible) {
            sendLastItemVisible = false;
            mListener.onLastItemVisible();
        }

        iListIndex = position;
    }

    @Override
    public int getItemCount() {
        return this.aGallery.size();
    }

    public void clearItems(ArrayList<ResultModel> aImages) {
        sendLastItemVisible = true;
        aGallery.clear();
        swapItems(aImages);
    }

    public ArrayList<ResultModel> getMovieList() {
        return aGallery;
    }

    public int getListIndex() {
        return iListIndex;
    }

    public void swapItems(ArrayList<ResultModel> aImages) {
        sendLastItemVisible = true;
        aGallery.addAll(aImages);
        DebugUtils.logDebug("TotalItems:: ", aGallery.size());
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return aGallery.isEmpty();
    }
}