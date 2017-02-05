package de.flo_aumeier.popularmoviesstage1;

import static android.R.attr.offset;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

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
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(mMoviePoster);

        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set a title for collapsing toolbar layout
        mCollapsingToolbarLayout.setTitle("Interstellar"); //TODO: Movie Title goes in here
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
