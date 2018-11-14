package id.nerdstudio.moviecatalogue;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;

import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_ARGS = "MOVIE";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        movie = getIntent().getParcelableExtra(MOVIE_ARGS);
        if (movie != null) {
            if(!movie.getPosterPath().isEmpty()) {
                final ImageView moviePoster = findViewById(R.id.movie_poster);
                Ion.with(this)
                        .load(AppConfig.getPoster(movie.getPosterPath()))
                        .asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                moviePoster.setImageBitmap(result);
                            }
                        });
            }
//            TextView movieTitle = findViewById(R.id.movie_title);
//            movieTitle.setText(movie.getTitle());
//            actionBar.setTitle(movie.getTitle());
//            TextView movieYear = findViewById(R.id.movie_year);
            String year = !movie.getReleaseDate().isEmpty() ? "(" + new DateTime(movie.getReleaseDate()).getYear() + ")" : "";
//            movieYear.setText(year);
            actionBar.setTitle(movie.getTitle() +" "+ year);
            TextView movieDescription = findViewById(R.id.movie_description);
            movieDescription.setText(movie.getOverview());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
