package com.freakybyte.movies.control.movies.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.freakybyte.movies.R;
import com.freakybyte.movies.control.BaseActivity;
import com.freakybyte.movies.control.adapter.MoviesRecyclerViewAdapter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesPresenter;
import com.freakybyte.movies.control.movies.constructor.GridMoviesView;
import com.freakybyte.movies.control.movies.impl.GridMoviesPresenterImpl;
import com.freakybyte.movies.listener.RecyclerViewListener;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.util.DebugUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.freakybyte.movies.util.ConstantUtils.FAVORITE;
import static com.freakybyte.movies.util.ConstantUtils.NEW;
import static com.freakybyte.movies.util.ConstantUtils.POPULAR;
import static com.freakybyte.movies.util.ConstantUtils.TOP_RATED;
import static com.freakybyte.movies.util.ConstantUtils.UPCOMING;

public class GridMoviesActivity extends BaseActivity implements GridMoviesView, RecyclerViewListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "GridMoviesActivity";

    public static final String TAG_MOVIE_PAGE = "MoviePage";
    public static final String TAG_LIST_INDEX = "ListIndex";
    public static final String TAG_FILTER_TYPE = "FilterType";
    public static final String TAG_SUBTITLE = "Subtitle";

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
    private int iListIndex = 0;
    private String mSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_title_grid);

        mPresenter = new GridMoviesPresenterImpl(this, this);

        gridImages.setLayoutManager(getStageGridLayoutManager());
        gridImages.setAdapter(getMoviesAdapter());
        gridImages.setHasFixedSize(true);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorScheme(new int[]{android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light});

        if (savedInstanceState != null) {
            iMoviePage = savedInstanceState.getInt(TAG_MOVIE_PAGE, 0);
            iListIndex = savedInstanceState.getInt(TAG_LIST_INDEX, 0);
            mSubtitle = savedInstanceState.getString(TAG_SUBTITLE, "");
            mPresenter.setFilterType(savedInstanceState.getInt(TAG_FILTER_TYPE, 0));
            ArrayList<MovieResponseModel> savedList = savedInstanceState.getParcelableArrayList(MovieResponseModel.TAG);
            updateGridMovies(savedList, mSubtitle);
        } else {
            mPresenter.getMovies(iMoviePage);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_filter_new:
                mPresenter.setFilterType(NEW);
                onRefresh();
                return true;
            case R.id.menu_item_filter_popular:
                mPresenter.setFilterType(POPULAR);
                onRefresh();
                return true;
            case R.id.menu_item_filter_top_rated:
                mPresenter.setFilterType(TOP_RATED);
                onRefresh();
                return true;
            case R.id.menu_item_filter_upcoming:
                mPresenter.setFilterType(UPCOMING);
                onRefresh();
                return true;
            case R.id.menu_item_filter_favorite:
                mPresenter.setFilterType(FAVORITE);
                onRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.toolbar)
    public void onClickToolbar() {
        iListIndex = 0;
        gridImages.smoothScrollToPosition(iListIndex);
    }

    @Override
    public void onItemClick(Object object) {
        MovieResponseModel movie = (MovieResponseModel) object;
        mPresenter.getMovieDetail(movie.getId());
        DebugUtils.logDebug(TAG, "Movie Selected:: " + movie.getTitle() + " Id:: " + movie.getId());
    }

    @Override
    public void onLastItemVisible() {
        iMoviePage++;
        mPresenter.getMovies(iMoviePage);
    }

    @Override
    public void onRefresh() {
        iMoviePage = 0;
        onLastItemVisible();
    }

    @Override
    public void updateGridMovies(ArrayList<MovieResponseModel> aGallery, String subtitle) {
        mSubtitle = subtitle;
        getSupportActionBar().setSubtitle(subtitle);

        if (iMoviePage == 1)
            mAdapter.clearItems(aGallery);
        else
            mAdapter.swapItems(aGallery);

        if (iListIndex != 0)
            gridImages.scrollToPosition(iListIndex);

        iListIndex = 0;
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
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(MovieResponseModel.TAG, mAdapter.getMovieList());
        savedInstanceState.putInt(TAG_MOVIE_PAGE, iMoviePage);
        savedInstanceState.putInt(TAG_LIST_INDEX, mAdapter.getListIndex());
        savedInstanceState.putString(TAG_SUBTITLE, mSubtitle);
        savedInstanceState.putSerializable(TAG_FILTER_TYPE, mPresenter.getFilterType());
        super.onSaveInstanceState(savedInstanceState);
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
}
