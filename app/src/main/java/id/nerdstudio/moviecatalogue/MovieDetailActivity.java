package id.nerdstudio.moviecatalogue;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.database.MovieHelper;
import id.nerdstudio.moviecatalogue.model.Movie;

import static android.provider.BaseColumns._ID;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.ADULT;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.CONTENT_URI;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.GENRE_IDS;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.ORIGINAL_LANGUAGE;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.ORIGINAL_TITLE;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.OVERVIEW;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.POPULARITY;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.POSTER_PATH;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.TITLE;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.VIDEO;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.VOTE_AVERAGE;
import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.VOTE_COUNT;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_ARGS = "MOVIE";
    public static final int FAVORITE = 100;
    private Movie movie;
    private MovieHelper movieHelper;
    private Menu menu;
    private boolean isFavorite = false;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.movie_description)
    TextView movieDescription;
    @BindView(R.id.movie_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.movie_favorite)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        movieHelper = new MovieHelper(this);
        movieHelper.open();

        movie = getIntent().getParcelableExtra(MOVIE_ARGS);
        if (movie != null) {
            Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI + "/" + movie.getId()), null, null, null, null);
            if (cursor != null) {
                isFavorite = cursor.getCount() > 0;
            }
            if (!movie.getReleaseDate().isEmpty()) {
                movieReleaseDate.setText(new DateTime(movie.getReleaseDate()).toString("E, dd-MM-yyyy",
                        AppSharedPreferences.getLanguage(this) == null ? Locale.ENGLISH : new Locale(AppSharedPreferences.getLanguage(this))
                ));
            }

            movieRating.setText(String.valueOf(movie.getVoteAverage()));
            if (!movie.getPosterPath().isEmpty()) {
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
            String year = !movie.getReleaseDate().isEmpty() ? "(" + new DateTime(movie.getReleaseDate()).getYear() + ")" : "";
            actionBar.setTitle(movie.getTitle() + " " + year);
            movieDescription.setText(movie.getOverview());
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavorite();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_share:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(android.content.Intent.EXTRA_SUBJECT, movie.getTitle());
                share.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.share_body, movie.getTitle(), AppConfig.BASE_WEB_URL + movie.getId()));
                startActivity(Intent.createChooser(share, getString(R.string.share_view)));
                break;
            case R.id.action_favorite:
                addToFavorite();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        toggleButtonMenu();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null) {
            movieHelper.close();
        }
    }

    public void addToFavorite() {
        ContentValues values = new ContentValues();
        values.put(_ID, movie.getId());
        values.put(VOTE_COUNT, movie.getVoteCount());
        values.put(VIDEO, movie.isVideo());// ? 1 : 0);
        values.put(VOTE_AVERAGE, movie.getVoteAverage());
        values.put(TITLE, movie.getTitle());
        values.put(POPULARITY, movie.getPopularity());
        values.put(POSTER_PATH, movie.getPosterPath());
        values.put(ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(ORIGINAL_TITLE, movie.getOriginalTitle());
        StringBuilder genreIds = new StringBuilder();
        for (int id : movie.getGenreIds()) {
            genreIds.append(id).append(",");
        }
        genreIds.deleteCharAt(genreIds.length() - 1);
        values.put(GENRE_IDS, genreIds.toString());
        values.put(BACKDROP_PATH, movie.getBackdropPath());
        values.put(ADULT, movie.isAdult()); // ? 1 : 0);
        values.put(OVERVIEW, movie.getOverview());
        values.put(RELEASE_DATE, movie.getReleaseDate());
        getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(this, getString(R.string.success_favorite, movie.getTitle()), Toast.LENGTH_SHORT).show();
    }

    public void removeFromFavorite() {
        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + movie.getId()), null, null);
        Toast.makeText(this, getString(R.string.success_delete_favorite, movie.getTitle()), Toast.LENGTH_SHORT).show();
    }

    private void toggleFavorite() {
        if (isFavorite) {
            removeFromFavorite();
        } else {
            addToFavorite();
        }
        isFavorite = !isFavorite;
        toggleButtonMenu();
    }

    public void toggleButtonMenu() {
        floatingActionButton.setImageResource(isFavorite ? R.drawable.ic_menu_favorite : R.drawable.ic_menu_favorite_empty);
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, isFavorite ? R.drawable.ic_menu_favorite : R.drawable.ic_menu_favorite_empty));
    }
}
