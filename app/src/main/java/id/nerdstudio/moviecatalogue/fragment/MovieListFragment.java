package id.nerdstudio.moviecatalogue.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MovieListFragment extends Fragment {
    private static final String ARG_CURRENT_TYPE = "CURRENT_TYPE";
    public static final int NOW_PLAYING = 0;
    public static final int UPCOMING = 1;
    private AppConfig.CurrentMovieType movieType;
    private ArrayList<Movie> movieList;
    private MovieAdapter mAdapter;
    private ProgressBar loadingView;
    private TextView emptyList;
    private View root;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(int movieType) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_TYPE, movieType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int type = getArguments().getInt(ARG_CURRENT_TYPE);
            switch (type) {
                case NOW_PLAYING:
                    movieType = AppConfig.CurrentMovieType.now_playing;
                    break;
                case UPCOMING:
                    movieType = AppConfig.CurrentMovieType.upcoming;
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_movie_list, container, false);
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(getActivity(), movieList);
        loadingView = root.findViewById(R.id.loading_view);
        emptyList = root.findViewById(R.id.empty_list);
        RecyclerView mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    private void fetchData() {
        loadingView.setVisibility(View.VISIBLE);
        movieList.clear();
        mAdapter.notifyDataSetChanged();
        String currentLanguage = AppSharedPreferences.getLanguage(getContext());
        AppConfig.Language language = currentLanguage == null ? AppConfig.Language.en :
                currentLanguage.equals("en") ? AppConfig.Language.en : AppConfig.Language.id;
        Ion.with(getActivity())
                .load(AppConfig.getCurrentMovies(movieType, language))
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
            fetchData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
