package id.nerdstudio.moviecatalogue.config;

import id.nerdstudio.moviecatalogue.BuildConfig;

public class AppConfig {
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_WEB_URL = "https://www.themoviedb.org/movie/";
    private static final String SEARCH_MOVIE = BASE_URL + "search/movie?api_key=" + API_KEY + "&query=";

    public enum CurrentMovieType {
        now_playing, upcoming
    }

    public enum Language {
        en, id
    }

    public static String getCurrentMovies(CurrentMovieType type, Language language){
        return BASE_URL + "movie/"+ type.name() + "?api_key=" + API_KEY + "&language="+language.name();
    }

    public static String withQuery(String query, Language language) {
        return SEARCH_MOVIE + query + "&language="+language.name();
    }

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public enum PosterType {
        w92, w154, w185, w342, w500, w780, original
    }

    public static String getPoster(String path) {
        return getPoster(PosterType.original, path);
    }

    private static String getPoster(PosterType type, String path) {
        return BASE_IMAGE_URL + type.name() + "/" + path;
    }
}
