package id.nerdstudio.moviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;
import java.util.Locale;

import id.nerdstudio.moviecatalogue.MovieDetailActivity;
import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<Movie> data;

    public MovieAdapter(Context context, List<Movie> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Movie movie = data.get(position);
        if (!movie.getPosterPath().isEmpty()) {
            Ion.with(context)
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
                    AppSharedPreferences.getLanguage(context) == null ? Locale.ENGLISH : new Locale(AppSharedPreferences.getLanguage(context))
            ));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
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
            Movie movie = data.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.movie_share:
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(android.content.Intent.EXTRA_SUBJECT, movie.getTitle());
                    share.putExtra(android.content.Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_body, movie.getTitle(), AppConfig.BASE_WEB_URL + movie.getId()));
                    context.startActivity(Intent.createChooser(share, context.getString(R.string.share_view)));
                    break;
                default:
                    context.startActivity(new Intent(context, MovieDetailActivity.class).putExtra(MovieDetailActivity.MOVIE_ARGS, movie));
                    break;
            }
        }
    }
}
