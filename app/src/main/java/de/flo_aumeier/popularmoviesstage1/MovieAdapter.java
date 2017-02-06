package de.flo_aumeier.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Society on 22.01.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    /**
     * Holding duplicate links to the interstellar poster
     * used for testing the recyclerview
     */

    final private ListItemClickListener mOnClickListener;

    private LinkedList<Movie> mMovies;

    public MovieAdapter(ListItemClickListener onClickListener, LinkedList<Movie> movieList) {
        mOnClickListener = onClickListener;
        mMovies = movieList;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item_view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutIdForGridItem, parent,
                shouldAttachToParentImmediately);
        PosterViewHolder posterViewHolder = new PosterViewHolder(view);
        return posterViewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPosterImageView;
        private TextView mMovieTitle;
        private Context mContext;
        private int width;
        private int height;

        public PosterViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_item_movie);
            mMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            String basePosterURL = "https://image.tmdb.org/t/p/w185/";
            String posterPath = movie.getUrlToPoster();
            String completeUrl = basePosterURL + posterPath;
            Log.d(TAG, "Loading Poster: " + completeUrl);
            Picasso.with(mContext)
                    .load(completeUrl)
                    .into(mPosterImageView);
            Log.d(TAG, "Setting title: " + movie.getTitle());
            mMovieTitle.setText(movie.getTitle());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
