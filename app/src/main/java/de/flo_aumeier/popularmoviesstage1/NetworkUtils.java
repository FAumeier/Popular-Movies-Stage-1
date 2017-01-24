package de.flo_aumeier.popularmoviesstage1;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Society on 22.01.2017.
 */

public class NetworkUtils {
    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3"; //EXAMPLE: https://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]&language=en-US&page=1
    private static final String API_KEY = "YOUR_API_KEY";

    private static final String PARAM_POPULAR_MOVIES = "/movie/popular";
    private static final String PARAM_BEST_RATED_MOVIES = "/movie/top_rated";
    private static final String PARAM_API_KEY = "?api_key=" + API_KEY;
    private static final String PARAM_LANGUAGE = "&language=";

    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendPath(PARAM_POPULAR_MOVIES)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, "en-EN")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
