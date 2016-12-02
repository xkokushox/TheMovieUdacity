package com.freakybyte.movies.control.movie.ui;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.freakybyte.movies.R;
import com.freakybyte.movies.control.BaseActivity;
import com.freakybyte.movies.control.adapter.ReviewRecyclerViewAdapter;
import com.freakybyte.movies.control.movie.constructor.MovieDetailPresenter;
import com.freakybyte.movies.control.movie.constructor.MovieDetailView;
import com.freakybyte.movies.control.movie.impl.MovieDetailPresenterImpl;
import com.freakybyte.movies.data.dao.FavoriteDao;
import com.freakybyte.movies.model.movie.MovieResponseModel;
import com.freakybyte.movies.model.review.ReviewMovieModel;
import com.freakybyte.movies.util.DateTimeUtils;
import com.freakybyte.movies.util.ImageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends BaseActivity implements MovieDetailView {
    public static final String TAG = "MovieDetailActivity";

    public static final String TAG_REVIEW_PAGE = "MovieReviewPage";

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.sv_movie_backdrop)
    public SimpleDraweeView svMovieBackdrop;
    @BindView(R.id.sv_movie_poster)
    public SimpleDraweeView svMoviePoster;
    @BindView(R.id.collapsing_toolbar)
    public CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.tv_movie_description)
    public TextView tvMovieDescription;
    @BindView(R.id.tv_movie_date)
    public TextView tvMovieDate;
    @BindView(R.id.tv_movie_duration)
    public TextView tvMovieDuration;
    @BindView(R.id.tv_movie_rating)
    public TextView tvMovieRating;
    @BindView(R.id.conversation)
    public RecyclerView gridImages;
    @BindView(R.id.scroll_view_movie_detail)
    public NestedScrollView scrollViewMovieDetail;
    @BindView(R.id.fb_movie_favorite)
    public FloatingActionButton fbMovieFavorite;

    private LinearLayoutManager mLayoutManager;
    private ReviewRecyclerViewAdapter mAdapter;

    private MovieResponseModel mMovie;
    private MovieDetailPresenter mPresenter;

    private FavoriteDao mFavoriteDao;

    private int iMoviePage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        mPresenter = new MovieDetailPresenterImpl(this, this);
        mFavoriteDao = FavoriteDao.getInstance(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mMovie = getIntent().getParcelableExtra(MovieResponseModel.TAG);

        setToolbarContent();
        updateMovieContent();

        gridImages.setLayoutManager(getStageGridLayoutManager());
        gridImages.setAdapter(getMoviesAdapter());
        gridImages.setHasFixedSize(true);

        if (savedInstanceState != null) {
            iMoviePage = savedInstanceState.getInt(TAG_REVIEW_PAGE, 0);
            ArrayList<ReviewMovieModel> savedList = savedInstanceState.getParcelableArrayList(MovieResponseModel.TAG);
            updateMovieReview(savedList);
        } else {
            mPresenter.getMovieReviews(mMovie.getId(), iMoviePage);
        }

        refreshFavButton(mFavoriteDao.isMovieFavorite(mMovie.getId()));

        Log.d(TAG, "Movie:: " + mMovie.getId());
    }

    public void setToolbarContent() {
        mCollapsingToolbar.setTitle(mMovie.getTitle());
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.black));
        mCollapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        final Postprocessor redMeshPostprocessor = new BasePostprocessor() {

            @Override
            public void process(Bitmap bitmap) {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @SuppressWarnings("ResourceType")
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch dominantSwatch = palette.getDominantSwatch();

                        if (dominantSwatch != null) {
                            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
                            mCollapsingToolbar.setContentScrimColor(dominantSwatch.getRgb());
                            mCollapsingToolbar.setCollapsedTitleTextColor(dominantSwatch.getTitleTextColor());
                            mCollapsingToolbar.setExpandedTitleColor(dominantSwatch.getTitleTextColor());
                        }

                        updateBackground(fbMovieFavorite, palette);
                        mCollapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimary)));
                    }
                });
            }
        };

        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ImageUtils.getMovieUri(false, mMovie.getBackdropPath())))
                .setPostprocessor(redMeshPostprocessor)
                .build();

        final PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(svMovieBackdrop.getController())
                        .build();

        svMovieBackdrop.setController(controller);
        svMoviePoster.setImageURI(ImageUtils.getMovieUri(false, mMovie.getPosterPath()));
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorAccent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

    private void updateMovieContent() {
        tvMovieDescription.setText(mMovie.getOverview());
        tvMovieDate.setText(DateTimeUtils.getYearByDate(mMovie.getReleaseDate()));
        tvMovieDuration.setText(String.format(getResources().getString(R.string.detail_movie_duration), mMovie.getRuntime()));
        tvMovieRating.setText(String.format(getResources().getString(R.string.detail_movie_rating), mMovie.getVoteAverage()));
    }


    @Override
    public void showLoader() {
    }

    @Override
    public void refreshFavButton(boolean isFavorite) {
        fbMovieFavorite.setImageResource(isFavorite ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite_no_selected);
    }

    @Override
    public void closeLoaders() {

    }

    @OnClick(R.id.fb_movie_favorite)
    public void setMovieFavorite() {
        if (mFavoriteDao.isMovieFavorite(mMovie.getId())) {
            mFavoriteDao.deleteFavorite(mMovie.getId());
            refreshFavButton(false);
        } else {
            mFavoriteDao.insertFavoriteMovie(mMovie.getId());
            refreshFavButton(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(MovieResponseModel.TAG, getMoviesAdapter().getReviews());
        savedInstanceState.putInt(TAG_REVIEW_PAGE, iMoviePage);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private LinearLayoutManager getStageGridLayoutManager() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(this);

        return mLayoutManager;
    }

    private ReviewRecyclerViewAdapter getMoviesAdapter() {
        if (mAdapter == null)
            mAdapter = new ReviewRecyclerViewAdapter();

        return mAdapter;
    }

    @Override
    public void updateMovieReview(ArrayList<ReviewMovieModel> aReviews) {
        getMoviesAdapter().swapItems(aReviews);
        scrollViewMovieDetail.scrollTo(0, 0);
        iMoviePage += iMoviePage;
    }
}
