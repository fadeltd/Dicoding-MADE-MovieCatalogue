package id.nerdstudio.moviecatalogue.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titles;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragment(String title, Fragment fragment) {
        if(titles == null){
            titles = new ArrayList<>();
        }
        titles.add(title);
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
