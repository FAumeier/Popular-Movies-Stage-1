package de.flo_aumeier.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Society on 22.01.2017.
 */

public class NetworkUtils {
    private static String TAG = NetworkUtils.class.getSimpleName();
    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie"; //EXAMPLE: https://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]&language=en-US&page=1
    private static final String API_KEY = "YOUR_API_KEY";

    private static final String PARAM_POPULAR_MOVIES = "popular";
    private static final String PARAM_BEST_RATED_MOVIES = "top_rated";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String LANGUAGE = "en-US";

    public static URL buildUrlPopularMovies() {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(PARAM_POPULAR_MOVIES)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, "Built URL: " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlMovieDetails(String movieId) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, "Built URL: " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
