package id.nerdstudio.moviecatalogue.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.adapter.MovieAdapter;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.model.Movie;
import id.nerdstudio.moviecatalogue.util.JsonUtil;

public class SearchMovieFragment extends Fragment {

    private MovieAdapter mAdapter;
    private ArrayList<Movie> movieList;
    private View root;
    private ProgressBar loadingView;
    private SearchView searchInput;
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
        searchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setIconified(false);
            }
        });
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                fetchData(query);
                return false;
            }
        });
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(getActivity(), movieList);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            searchInput.setQuery(savedInstanceState.getString("searchInput"), false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchInput", searchInput.getQuery().toString());
    }
}
