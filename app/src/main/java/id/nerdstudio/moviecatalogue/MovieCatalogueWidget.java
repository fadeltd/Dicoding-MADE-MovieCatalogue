package id.nerdstudio.moviecatalogue;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import id.nerdstudio.moviecatalogue.model.Movie;

import static id.nerdstudio.moviecatalogue.database.DatabaseContract.MovieColumns.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class MovieCatalogueWidget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "id.nerdstudio.moviecatalogue.TOAST_ACTION";
    public static final String EXTRA_ITEM = "id.nerdstudio.moviecatalogue.EXTRA_ITEM";
    private static Cursor movieList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        movieList = context.getContentResolver().query(CONTENT_URI, null, null, null, null);

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_catalogue_widget);

        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, MovieCatalogueWidget.class);

        toastIntent.setAction(MovieCatalogueWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            if (!movieList.moveToPosition(viewIndex)) {
                throw new IllegalStateException("Position invalid");
            }
            Movie movie = new Movie(movieList);
            // context.startActivity(new Intent(context, MovieDetailActivity.class).putExtra(MovieDetailActivity.MOVIE_ARGS, movie));
            Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
}

