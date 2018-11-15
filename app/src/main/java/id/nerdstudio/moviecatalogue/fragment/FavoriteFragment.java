package id.nerdstudio.moviecatalogue.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.adapter.FavoriteMovieAdapter;

import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class FavoriteFragment extends Fragment {

    public Cursor movieList;
    public FavoriteMovieAdapter mAdapter;
    public TextView emptyList;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadFavoriteMovies().execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        emptyList = view.findViewById(R.id.empty_list);
        mAdapter = new FavoriteMovieAdapter(getActivity());
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class LoadFavoriteMovies extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getActivity().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            movieList = movies;
            mAdapter.setMovieList(movieList);
            mAdapter.notifyDataSetChanged();
            if (movieList.getCount() == 0) {
                emptyList.setVisibility(View.VISIBLE);
            } else {
                emptyList.setVisibility(View.GONE);
            }
        }
    }
}
