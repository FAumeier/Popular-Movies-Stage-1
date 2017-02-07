package de.flo_aumeier.popularmoviesstage1;

import static android.R.attr.offset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity  implements AppBarLayout.OnOffsetChangedListener  {

    private static final float THRESHOLD_PERCENTAGE = 0.001F;

    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ImageView mMovieStill;
    private ImageView mMoviePoster;

    private TextView mPlot;
    private TextView mReleaseDate;
    private TextView mRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MovieActivity.this;

        // Get the widget reference from XML layout
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mMovieStill = (ImageView) findViewById(R.id.iv_movie_poster);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342//xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg").into(mMovieStill);
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster_toolbar);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_rating);
        // Set the support action bar
        setSupportActionBar(mToolbar);
        Intent intentFromMainActivity = getIntent();
        double rating = intentFromMainActivity.getDoubleExtra(MainActivity.INTENT_EXTRA_MOVIE_RATING, 0.0);
        String title = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_TITLE);
        String pathToPoster = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_POSTER);
        String releaseDate = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_RELEASE_DATE);
        String plot = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_PLOT);
        // Set a title for collapsing toolbar layout
        mCollapsingToolbarLayout.setTitle(title);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w185/" + pathToPoster).into(mMoviePoster);
        mPlot.setText(plot);
        mReleaseDate.setText(releaseDate);
        mRating.setText(String.valueOf(rating));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float progressPercentage = (float) (Math.abs(offset)/maxScroll);

        if (progressPercentage >= THRESHOLD_PERCENTAGE) {
            //start an alpha animation on your ImageView here (i.e. fade out)
            mMoviePoster.setVisibility(View.INVISIBLE);
        } else {
            //Add an opposite animation here (i.e. it fades back in again)
            mMoviePoster.setVisibility(View.VISIBLE);
        }
    }
}
