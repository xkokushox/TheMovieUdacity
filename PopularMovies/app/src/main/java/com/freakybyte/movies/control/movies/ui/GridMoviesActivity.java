package com.freakybyte.movies.control.movies.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.freakybyte.movies.R;
import com.freakybyte.movies.control.BaseActivity;
import com.freakybyte.movies.control.adapter.MoviesRecyclerViewAdapter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesPresenter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesView;
import com.freakybyte.movies.control.movies.impl.GridMoviesPresenterImpl;
import com.freakybyte.movies.listener.RecyclerViewListener;
import com.freakybyte.movies.model.ResultModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridMoviesActivity extends BaseActivity implements GridMoviesView, RecyclerViewListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recycler_grid_images)
    public RecyclerView gridImages;
    @BindView(R.id.swipe_container)
    public SwipeRefreshLayout swipeContainer;

    private StaggeredGridLayoutManager stageGridLayoutManager;
    private MoviesRecyclerViewAdapter mAdapter;
    private GridMoviesPresenter mPresenter;

    private int iMoviePage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mPresenter = new GridMoviesPresenterImpl(this, this);

        gridImages.setLayoutManager(getStageGridLayoutManager());
        gridImages.setAdapter(getMoviesAdapter());
        gridImages.setHasFixedSize(true);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorScheme(new int[]{android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light});

        mPresenter.getPopularMovies(iMoviePage);
    }

    @Override
    public void onItemClick(Object object) {

    }

    @Override
    public void onLastItemVisible() {
        iMoviePage++;
        mPresenter.getPopularMovies(iMoviePage);
    }

    @Override
    public void onRefresh() {
        iMoviePage = 0;
        onLastItemVisible();
    }

    @Override
    public void updateGridMovies(List<ResultModel> aGallery) {
        if (iMoviePage == 1)
            mAdapter.clearItems(aGallery);
        else
            mAdapter.swapItems(aGallery);


    }

    @Override
    public void showLoader() {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });
    }

    @Override
    public void closeLoaders() {
        swipeContainer.setRefreshing(false);

    }

    private StaggeredGridLayoutManager getStageGridLayoutManager() {
        if (stageGridLayoutManager == null)
            stageGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        return stageGridLayoutManager;
    }

    private MoviesRecyclerViewAdapter getMoviesAdapter() {
        if (mAdapter == null)
            mAdapter = new MoviesRecyclerViewAdapter(this);

        return mAdapter;
    }

    @Override
    public void onBackPressed() {
        if (swipeContainer.isRefreshing()) {
            mPresenter.cancelDownload();
            closeLoaders();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
