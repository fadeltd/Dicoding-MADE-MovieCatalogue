package id.nerdstudio.moviecatalogue.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.adapter.PagerAdapter;

public class MainFragment extends Fragment {
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(getResources().getString(R.string.now_playing), MovieListFragment.newInstance(MovieListFragment.NOW_PLAYING));
        adapter.addFragment(getResources().getString(R.string.upcoming), MovieListFragment.newInstance(MovieListFragment.UPCOMING));
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
