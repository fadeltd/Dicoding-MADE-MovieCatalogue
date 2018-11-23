package id.nerdstudio.moviecatalogue.adapter;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import id.nerdstudio.moviecatalogue.MovieCatalogueWidget;
import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.config.AppConfig;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.model.Movie;

import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private List<Movie> movieList;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        Cursor movieCursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);

        if (movieList == null) movieList = new ArrayList<>();
        else movieList.clear();
        for (int i = 0; i < movieCursor.getCount(); i++) {
            if (!movieCursor.moveToPosition(i)) {
                throw new IllegalStateException("Position invalid");
            }
            movieList.add(new Movie(movieCursor));
        }

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie movie = movieList.get(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.movie_catalogue_widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Ion.with(mContext)
                    .load(AppConfig.getPoster(movie.getPosterPath()))
                    .asBitmap()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Widget Load Error", e.getMessage());
        }

        remoteViews.setImageViewBitmap(R.id.movie_poster, bitmap);
        remoteViews.setTextViewText(R.id.movie_title, movie.getTitle());
        remoteViews.setTextViewText(R.id.movie_description, movie.getOverview());
        if (!movie.getReleaseDate().isEmpty()) {
            remoteViews.setTextViewText(R.id.movie_release_date, (new DateTime(movie.getReleaseDate()).toString("E, dd-MM-yyyy",
                    AppSharedPreferences.getLanguage(mContext) == null ? Locale.ENGLISH : new Locale(AppSharedPreferences.getLanguage(mContext))
            )));
        }

        Bundle extras = new Bundle();
        extras.putInt(MovieCatalogueWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.movie_layout, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
