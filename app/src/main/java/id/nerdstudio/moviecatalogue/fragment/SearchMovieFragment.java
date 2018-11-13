package id.nerdstudio.moviecatalogue.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.adapter.MovieAdapter;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.model.Movie;
import id.nerdstudio.moviecatalogue.util.JsonUtil;

public class SearchMovieFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieList;
    private View root;
    private ProgressBar loadingView;
    private EditText searchInput;
    private TextView emptyList;

    public SearchMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_movie, container, false);

        root = view.findViewById(android.R.id.content);
        loadingView = view.findViewById(R.id.loading_view);
        emptyList = view.findViewById(R.id.empty_list);
        searchInput = view.findViewById(R.id.search_input);
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
        view.findViewById(R.id.search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData(searchInput.getText().toString());
            }
        });
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(getActivity(), movieList);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
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
        String currentLanguage = AppSharedPreferences.getLanguage(getContext());
        AppConfig.Language language = currentLanguage == null ? AppConfig.Language.en :
                currentLanguage.equals("en") ? AppConfig.Language.en : AppConfig.Language.id;
        Ion
                .with(this)
                .load(AppConfig.withQuery(query, language))
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
                            if (JsonUtil.isNotNull(result, "results")) {
                                JsonArray movies = result.get("results").getAsJsonArray();
                                for (int i = 0; i < movies.size(); i++) {
                                    JsonObject movie = movies.get(i).getAsJsonObject();
                                    movieList.add(Movie.fromJson(movie));
                                }
                                mAdapter.notifyDataSetChanged();
                                emptyList.setVisibility(View.GONE);
                            } else {
                                emptyList.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
}
