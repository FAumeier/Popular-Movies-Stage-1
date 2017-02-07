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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    public static final String INTENT_EXTRA_MOVIE_TITLE = "EXTRA_MOVIE_TITLE";
    public static final String INTENT_EXTRA_MOVIE_RATING = "EXTRA_MOVIE_RATING";
    public static final String INTENT_EXTRA_MOVIE_POSTER = "EXTRA_MOVIE_POSTER";
    public static final String INTENT_EXTRA_MOVIE_RELEASE_DATE = "EXTRA_MOVIE_RELEASE_DATE";
    public static final String INTENT_EXTRA_MOVIE_PLOT = "EXTRA_MOVIE_PLOT";
    public static final String INTENT_EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    private final static String TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

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

        int spanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                spanCount);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        fetchPopularMovies();
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
        if (itemThatWasClickedId == R.id.action_refresh) {
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

    private void fetchPopularMovies() {
        URL urlPopularMovies = NetworkUtils.buildUrlPopularMovies();
        Log.d(TAG, "Doing PopularMoviesQuery: " + urlPopularMovies.toString());
        try {
            mMovies = new PopularMoviesQueryTask().execute(urlPopularMovies).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        mMovieAdapter = new MovieAdapter(this, mMovies);
        mRecyclerView.setAdapter(mMovieAdapter);

    }

    public class PopularMoviesQueryTask extends AsyncTask<URL, Void, LinkedList<Movie>> {

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
            String popularMoviesString = queryResults;
            LinkedList<Movie> movieLinkedList = parseJsonResult(popularMoviesString);
            return movieLinkedList;
        }

        private LinkedList<Movie> parseJsonResult(String movieJSON) {
            LinkedList<Movie> movies = new LinkedList<>();
            try {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }
    }
}
