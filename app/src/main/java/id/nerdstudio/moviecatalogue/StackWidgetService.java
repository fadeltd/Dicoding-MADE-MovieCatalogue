package id.nerdstudio.moviecatalogue;

import android.content.Intent;
import android.widget.RemoteViewsService;

import java.util.List;

import id.nerdstudio.moviecatalogue.adapter.StackRemoteViewsFactory;
import id.nerdstudio.moviecatalogue.model.Movie;

public class StackWidgetService extends RemoteViewsService {
    private List<Movie> movieList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(getApplicationContext(), intent);
    }
}