package de.flo_aumeier.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import de.flo_aumeier.popularmoviesstage1.Model.Movie;
import de.flo_aumeier.popularmoviesstage1.Utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    public static final String INTENT_EXTRA_MOVIE_TITLE = "EXTRA_MOVIE_TITLE";
    public static final String INTENT_EXTRA_MOVIE_RATING = "EXTRA_MOVIE_RATING";
    public static final String INTENT_EXTRA_MOVIE_POSTER = "EXTRA_MOVIE_POSTER";
    public static final String INTENT_EXTRA_MOVIE_RELEASE_DATE = "EXTRA_MOVIE_RELEASE_DATE";
    public static final String INTENT_EXTRA_MOVIE_PLOT = "EXTRA_MOVIE_PLOT";
    public static final String INTENT_EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    private final static String TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mPopularMoviesAdapter;
    private MovieAdapter mBestRatedMoviesAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;
    private RelativeLayout mErrorLayout;
    private ImageButton mReloadButton;

    private Toast mToast;
    private LinkedList<Movie> mMovies;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getViewsFromXML();
        setUpReloadButton();

        try {
            fetchPopularMovies();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (NoNetworkConnectionException e) {
            e.printStackTrace();
            displayErrorLayout();
        }

        setUpView();
    }

    private void setUpReloadButton() {
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fetchPopularMovies();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                } catch (NoNetworkConnectionException e) {
                    displayErrorLayout();
                    e.printStackTrace();
                }
            }
        });
    }

    private void getViewsFromXML() {
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mErrorLayout = (RelativeLayout) findViewById(R.id.layout_main_error);
        mReloadButton = (ImageButton) findViewById(R.id.ib_cloud_reload);
    }

    private void setUpView() {
        mPopularMoviesAdapter = new MovieAdapter(this, mMovies);
        mRecyclerView.setAdapter(mPopularMoviesAdapter);
        int spanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        switch (itemThatWasClickedId) {
            case R.id.action_refresh:
                Log.d(TAG, "Option Refresh clicked");
                try {
                    fetchPopularMovies();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                } catch (NoNetworkConnectionException e) {
                    displayErrorLayout();
                }
                mPopularMoviesAdapter = new MovieAdapter(this, mMovies);
                updateRecyclerView(mPopularMoviesAdapter);
                return true;
            case R.id.sort_order_best_rated:
                Log.d(TAG, "Order by: Best rated");
                try {
                    fetchBestRatedMovies();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                } catch (NoNetworkConnectionException e) {
                    displayErrorLayout();
                }
                mBestRatedMoviesAdapter = new MovieAdapter(this, mMovies);
                updateRecyclerView(mBestRatedMoviesAdapter);
                return true;
            case R.id.sort_order_most_popular:
                Log.d(TAG, "Order by: Most popular");
                try {
                    fetchPopularMovies();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                } catch (NoNetworkConnectionException e) {
                    displayErrorLayout();
                }
                mPopularMoviesAdapter = new MovieAdapter(this, mMovies);
                updateRecyclerView(mPopularMoviesAdapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = mMovies.get(clickedItemIndex);
        double rating = movie.getRating();
        String title = movie.getTitle();
        String pathToPoster = movie.getUrlToPoster();
        String releaseDate = movie.getReleaseDate();
        String plot = movie.getPlot();
        int id = movie.getId();
        Intent movieDetailActivityIntent = getMovieIntent(rating, title, pathToPoster, releaseDate,
                plot, id);
        startActivity(movieDetailActivityIntent);
    }

    @NonNull
    private Intent getMovieIntent(double rating, String title, String pathToPoster,
            String releaseDate, String plot, int id) {
        Intent movieDetailActivityIntent = new Intent(mContext, MovieActivity.class);
        movieDetailActivityIntent.putExtra(INTENT_EXTRA_MOVIE_TITLE, title);
        movieDetailActivityIntent.putExtra(INTENT_EXTRA_MOVIE_RELEASE_DATE, releaseDate);
        movieDetailActivityIntent.putExtra(INTENT_EXTRA_MOVIE_RATING, rating);
        movieDetailActivityIntent.putExtra(INTENT_EXTRA_MOVIE_PLOT, plot);
        movieDetailActivityIntent.putExtra(INTENT_EXTRA_MOVIE_POSTER, pathToPoster);
        movieDetailActivityIntent.putExtra(INTENT_EXTRA_MOVIE_ID, id);
        return movieDetailActivityIntent;
    }

    private void fetchPopularMovies()
            throws ExecutionException, InterruptedException, NoNetworkConnectionException {
        if (NetworkUtils.isOnline(mContext)) {
            hideErrorLayout();
            URL urlPopularMovies = NetworkUtils.buildUrlPopularMovies();
            Log.d(TAG, "Doing PopularMoviesQuery: " + urlPopularMovies.toString());
            mMovies = new MoviesQueryTask().execute(urlPopularMovies).get();
            setUpView();
        } else {
            throw new NoNetworkConnectionException("No Internet Connection");
        }

    }

    private void fetchBestRatedMovies()
            throws ExecutionException, InterruptedException, NoNetworkConnectionException {
        if (NetworkUtils.isOnline(mContext)) {
            hideErrorLayout();
            URL urlBestRatedMovies = NetworkUtils.buildUrlBestRatedMovies();
            Log.d(TAG, "Doing BestRatedMoviesQuery: " + urlBestRatedMovies.toString());
            mMovies = new MoviesQueryTask().execute(urlBestRatedMovies).get();
            setUpView();
        } else {
            throw new NoNetworkConnectionException("No Internet Connection");
        }

    }

    private void hideErrorLayout() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void displayErrorLayout() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void updateRecyclerView(MovieAdapter adapter) {
        mRecyclerView.swapAdapter(adapter, false);
    }

    private LinkedList<Movie> parseJsonResult(String movieJSON) throws JSONException {
        LinkedList<Movie> movies = new LinkedList<>();
        JSONObject popularMovies = new JSONObject(movieJSON);
        JSONArray moviesArray = popularMovies.getJSONArray("results");
        Log.d(TAG, "Result size: " + moviesArray.length());
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            String title = movie.getString("original_title");
            String posterPath = movie.getString("poster_path");
            String plot = movie.getString("overview");
            String releaseDate = movie.getString("release_date");
            int id = movie.getInt("id");
            double rating = movie.getDouble("vote_average");
            Movie newMovie = new Movie(id, title, releaseDate, posterPath, plot, rating);
            Log.d(TAG, "Adding new Movie: " + newMovie.toString());
            movies.add(newMovie);
        }
        return movies;
    }

    public class MoviesQueryTask extends AsyncTask<URL, Void, LinkedList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected LinkedList<Movie> doInBackground(URL... params) {
            URL queryUrl = params[0];
            String queryResults = null;
            try {
                queryResults = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Results: " + queryResults);
            String JSONMovies = queryResults;
            try {
                return parseJsonResult(JSONMovies);
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(LinkedList<Movie> movies) {
            super.onPostExecute(movies);
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.GONE);
        }
    }

}
