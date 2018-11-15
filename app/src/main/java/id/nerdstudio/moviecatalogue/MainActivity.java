package id.nerdstudio.moviecatalogue;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import java.util.Locale;

import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.fragment.FavoriteFragment;
import id.nerdstudio.moviecatalogue.fragment.MainFragment;
import id.nerdstudio.moviecatalogue.fragment.SearchMovieFragment;
import id.nerdstudio.moviecatalogue.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mContent;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage(false);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
        } else {
            mContent = new MainFragment();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mContent instanceof MainFragment) {
            setTitle(R.string.home);
            navigationView.setCheckedItem(R.id.nav_home);
        } else if (mContent instanceof SearchMovieFragment) {
            setTitle(R.string.search);
            navigationView.setCheckedItem(R.id.nav_search);
        } else if (mContent instanceof FavoriteFragment) {
            setTitle(R.string.favorite);
            navigationView.setCheckedItem(R.id.nav_favorite);
        } else if (mContent instanceof SettingsFragment) {
            setTitle(R.string.settings);
            navigationView.setCheckedItem(R.id.nav_settings);
        }
        replaceFragment(mContent);
    }

    public void setLanguage(boolean restartActivity) {
        String lang = AppSharedPreferences.getLanguage(this) == null ? "en" : AppSharedPreferences.getLanguage(this);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(lang.toLowerCase()));
        } else {
            configuration.locale = new Locale(lang);
        }
        resources.updateConfiguration(configuration, displayMetrics);
        if (restartActivity) {
            restartActivity();
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                mContent = new MainFragment();
                break;
            case R.id.nav_search:
                mContent = new SearchMovieFragment();
                break;
            case R.id.nav_favorite:
                mContent = new FavoriteFragment();
                break;
            case R.id.nav_settings:
                mContent = new SettingsFragment();
                navigationView.setCheckedItem(R.id.nav_settings);
                break;
        }
        setTitle(item.getTitle());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        replaceFragment(mContent);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mContent instanceof FavoriteFragment){
            mContent = new FavoriteFragment();
            replaceFragment(mContent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragment", mContent);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
