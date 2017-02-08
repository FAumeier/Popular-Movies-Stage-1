/*
* Copyright (C) 2017 Aumeier Florian
*/
package de.flo_aumeier.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import de.flo_aumeier.popularmoviesstage1.Utils.NetworkUtils;
/*
* Displays detailed information for a specific movie.
*/
public class MovieActivity extends AppCompatActivity {
    private static final String TAG = MovieActivity.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;
    private CoordinatorLayout mCLayout;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ImageView mMovieBackdrop;
    private ImageView mMoviePoster;

    private TextView mPlot;
    private TextView mReleaseDate;
    private TextView mRating;
    /**
     * Displays an error if one is encountered
     */
    private Snackbar mErrorSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MovieActivity.this;

        getXMLReferences();
        setupActionBar();

        // Get Movie details of clicked movie from intent
        Intent intentFromMainActivity = getIntent();
        getMovieDetails(intentFromMainActivity);
        //get backdrop for clicked movie
        int movieId = intentFromMainActivity.getIntExtra(MainActivity.INTENT_EXTRA_MOVIE_ID, 0);
        if (NetworkUtils.isOnline(this)) {
            URL urlToMovieStill = NetworkUtils.buildUrlMovieStills(String.valueOf(movieId));
            new MovieStillTask().execute(urlToMovieStill);
        } else {
            mErrorSnackBar = Snackbar.make(findViewById(R.id.coordinator_layout),
                    R.string.error, Snackbar.LENGTH_LONG);
            mErrorSnackBar.show();
        }
    }

    private void setupActionBar() {
        // Set the support action bar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getXMLReferences() {
        // Get the widget reference from XML layout
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(
                R.id.collapsing_toolbar_layout_movie_details);
        mMovieBackdrop = (ImageView) findViewById(R.id.iv_movie_poster);
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster_toolbar);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_rating);
    }

    private void getMovieDetails(Intent intentFromMainActivity) {
        double rating = intentFromMainActivity.getDoubleExtra(
                MainActivity.INTENT_EXTRA_MOVIE_RATING, 0.0);
        String title = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_TITLE);
        String pathToPoster = intentFromMainActivity.getStringExtra(
                MainActivity.INTENT_EXTRA_MOVIE_POSTER);
        String releaseDate = intentFromMainActivity.getStringExtra(
                MainActivity.INTENT_EXTRA_MOVIE_RELEASE_DATE);
        String plot = intentFromMainActivity.getStringExtra(MainActivity.INTENT_EXTRA_MOVIE_PLOT);
        setViews(rating, title, pathToPoster, releaseDate, plot);
    }

    private void setViews(double rating, String title, String pathToPoster, String releaseDate,
            String plot) {
        // Set a title for collapsing toolbar layout
        mCollapsingToolbarLayout.setTitle(title);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342/" + pathToPoster).into(
                mMoviePoster);
        mPlot.setText(plot);
        mReleaseDate.setText(releaseDate);
        mRating.setText(String.valueOf(rating));
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
                backdrop = parseResult(result);
            } catch (IOException e) {
                Log.e(TAG, "AsyncTask DoInBackground error in getResponseFromHttpUrl", e);
            } catch (JSONException e) {
                Log.e(TAG, "There was a problem while parsing JSON", e);
            }
            return result;
        }

        private String parseResult(String backdropsJSON) throws JSONException {
            String pathToBackdrop = null;
            JSONObject recievedJSON = new JSONObject(backdropsJSON);
            JSONArray stills = recievedJSON.getJSONArray("backdrops");
            JSONObject backdrop = stills.getJSONObject(2);
            pathToBackdrop = backdrop.getString("file_path");
            return pathToBackdrop;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setBackdropImage();
        }

        private void setBackdropImage() {
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w342/" + backdrop).into(
                    mMovieBackdrop);
        }
    }

}
