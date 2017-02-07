package de.flo_aumeier.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Society on 22.01.2017.
 */

public class NetworkUtils {
    private static String TAG = NetworkUtils.class.getSimpleName();
    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie"; //EXAMPLE: https://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]&language=en-US&page=1
    private static final String API_KEY = "API";

    private static final String PARAM_POPULAR_MOVIES = "popular";
    private static final String PARAM_BEST_RATED_MOVIES = "top_rated";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String LANGUAGE = "en-US";
    private static final String PARAM_PAGE = "page";

    public static URL buildUrlPopularMovies() {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(PARAM_POPULAR_MOVIES)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, "1")
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

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
