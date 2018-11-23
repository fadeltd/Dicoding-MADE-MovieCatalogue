package id.nerdstudio.moviecatalogue.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;

import java.util.Locale;

import id.nerdstudio.moviecatalogue.MovieDetailActivity;
import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.model.Movie;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder> {
    private Activity activity;
    private Cursor movieList;

    public FavoriteMovieAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_movie, parent, false));

    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (!movie.getPosterPath().isEmpty()) {
            Ion.with(activity)
                    .load(AppConfig.getPoster(movie.getPosterPath()))
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            holder.moviePoster.setImageBitmap(result);
                        }
                    });
        }
        holder.movieTitle.setText(movie.getTitle());
        holder.movieDescription.setText(movie.getOverview());
        if (!movie.getReleaseDate().isEmpty()) {
            holder.movieReleaseDate.setText(new DateTime(movie.getReleaseDate()).toString("E, dd-MM-yyyy",
                    AppSharedPreferences.getLanguage(activity) == null ? Locale.ENGLISH : new Locale(AppSharedPreferences.getLanguage(activity))
            ));
        }
    }

    @Override
    public int getItemCount() {
        if (movieList == null) return 0;
        return movieList.getCount();
    }

    private Movie getItem(int position) {
        if (!movieList.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(movieList);
    }

    public void setMovieList(Cursor movieList) {
        this.movieList = movieList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieDescription;
        TextView movieReleaseDate;
        LinearLayout movieDetail;
        LinearLayout movieShare;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
            movieDescription = itemView.findViewById(R.id.movie_description);
            movieReleaseDate = itemView.findViewById(R.id.movie_release_date);
            movieDetail = itemView.findViewById(R.id.movie_detail);
            movieShare = itemView.findViewById(R.id.movie_share);

            itemView.setOnClickListener(this);
            movieDetail.setOnClickListener(this);
            movieShare.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = getItem(getAdapterPosition());
            switch (view.getId()) {
                case R.id.movie_share:
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(android.content.Intent.EXTRA_SUBJECT, movie.getTitle());
                    share.putExtra(android.content.Intent.EXTRA_TEXT, activity.getResources().getString(R.string.share_body, movie.getTitle(), AppConfig.BASE_WEB_URL + movie.getId()));
                    activity.startActivity(Intent.createChooser(share, activity.getString(R.string.share_view)));
                    break;
                default:
                    activity.startActivityForResult (new Intent(activity, MovieDetailActivity.class).putExtra(MovieDetailActivity.MOVIE_ARGS, movie), MovieDetailActivity.FAVORITE);
                    break;
            }
        }
    }
}

