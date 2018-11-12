package id.nerdstudio.moviecatalogue;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;

import id.nerdstudio.moviecatalogue.adapter.MovieAdapter;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.model.Movie;
import id.nerdstudio.moviecatalogue.util.JsonUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private View root;
    private ProgressBar loadingView;
    private EditText searchInput;
    private TextView emptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(android.R.id.content);
        loadingView = findViewById(R.id.loading_view);
        emptyList = findViewById(R.id.empty_list);
        searchInput = findViewById(R.id.search_input);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchData(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData(searchInput.getText().toString());
            }
        });
        mAdapter = new MovieAdapter(this, movieList);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        ((SearchView) menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                fetchData(newText);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

    private void fetchData(String query) {
        loadingView.setVisibility(View.VISIBLE);
        movieList.clear();
        mAdapter.notifyDataSetChanged();
        Ion
                .with(this)
                .load(AppConfig.withQuery(query))
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> response) {
                        loadingView.setVisibility(View.GONE);
                        if (e != null) {
                            Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
                        } else {
                            JsonObject result = response.getResult();
                            if (JsonUtil.isNotNull("results", result)) {
                                JsonArray movies = result.get("results").getAsJsonArray();
                                for (int i = 0; i < movies.size(); i++) {
                                    JsonObject movie = movies.get(i).getAsJsonObject();
                                    long voteCount = movie.get("vote_count").getAsLong();
                                    long id = movie.get("id").getAsLong();
                                    boolean video = movie.get("video").getAsBoolean();
                                    float voteAverage = movie.get("vote_average").getAsFloat();
                                    String title = movie.get("title").getAsString();
                                    double popularity = movie.get("popularity").getAsDouble();
                                    String posterPath = movie.get("poster_path").getAsString();
                                    String originalLanguage = movie.get("original_language").getAsString();
                                    String originalTitle = movie.get("original_title").getAsString();
                                    int[] genreIds = new Gson().fromJson(movie.get("genre_ids"), int[].class);
                                    String backdropPath = movie.get("backdrop_path").getAsString();
                                    boolean adult = movie.get("adult").getAsBoolean();
                                    String overview = movie.get("overview").getAsString();
                                    String releaseDate = movie.get("release_date").getAsString();
                                    movieList.add(new Movie(voteCount, id, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, genreIds, backdropPath, adult, overview, releaseDate));
                                }
                                mAdapter.notifyDataSetChanged();
                                emptyList.setVisibility(View.GONE);
                            }else{
                                emptyList.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
}
