package de.flo_aumeier.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        int spacing = Math.round(64 * getResources().getDisplayMetrics().density); //8dp
        boolean includeEdge = true;
        int spanCount = 2;
        //mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge)); //See: http://stackoverflow.com/a/30701422
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
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
            makePopularMoviesQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }

    private void makePopularMoviesQuery() {
        URL urlPopularMovies = NetworkUtils.buildUrlPopularMovies();
        Log.d(TAG, "Doing PopularMoviesQuery: " + urlPopularMovies.toString());
        new PopularMoviesQueryTask().execute(urlPopularMovies);
    }

    public class PopularMoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL queryUrl = params[0];
            String queryResults = null;
            try {
                queryResults = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return queryResults;
        }
    }
}
