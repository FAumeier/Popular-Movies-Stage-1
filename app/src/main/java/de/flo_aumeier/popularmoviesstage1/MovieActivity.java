package de.flo_aumeier.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

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
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342//xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg").into(mMoviePoster);

        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set a title for collapsing toolbar layout
        mCollapsingToolbarLayout.setTitle("Kojima");
    }
}
