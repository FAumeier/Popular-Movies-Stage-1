package de.flo_aumeier.popularmoviesstage1;

import static android.R.attr.offset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MovieActivity extends AppCompatActivity {

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
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster_toolbar);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_rating);
        // Set the support action bar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentFromMainActivity = getIntent();
        double rating = intentFromMainActivity.getDoubleExtra(MainActivity.INTENT_EXTRA_MOVIE_RATING, 0.0);
        String title = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_TITLE);
        String pathToPoster = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_POSTER);
        String releaseDate = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_RELEASE_DATE);
        String plot = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_PLOT);
        int movieId = intentFromMainActivity.getIntExtra(MainActivity.INTENT_EXTRA_MOVIE_ID, 0);
        // Set a title for collapsing toolbar layout
        mCollapsingToolbarLayout.setTitle(title);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342/" + pathToPoster).into(mMoviePoster);
        mPlot.setText(plot);
        mReleaseDate.setText(releaseDate);
        mRating.setText(String.valueOf(rating));
        //get movie still
        URL urlToMovieStill = NetworkUtils.buildUrlMovieStills(String.valueOf(movieId));
        new MovieStillTask().execute(urlToMovieStill);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieStillTask extends AsyncTask<URL, Void, String> {
        private String backdrop;
        @Override
        protected String doInBackground(URL... params) {
            URL queryURL = params[0];
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(queryURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            backdrop = parseResult(result);
            return result;
        }

        private String parseResult(String backdropsJSON) {
            String pathToBackdrop = null;
            try {
                JSONObject recievedJSON = new JSONObject(backdropsJSON);
                JSONArray stills = recievedJSON.getJSONArray("backdrops");
                JSONObject backdrop = stills.getJSONObject(2);
                pathToBackdrop = backdrop.getString("file_path");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pathToBackdrop;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342/" + backdrop).into(mMovieStill);
        }
    }

}
