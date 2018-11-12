package id.nerdstudio.moviecatalogue.config;

public class AppConfig {
    private static final String API_KEY = "f6f7eb9cb35e30e786faa519f685b4b2";
    private static final String BASE_URL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=";

    public static String withQuery(String query) {
        return BASE_URL + query;
    }

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public enum PosterType {
        w92, w154, w185, w342, w500, w780, original
    }

    public static String getPoster(String path) {
        return getPoster(PosterType.original, path);
    }

    public static String getPoster(PosterType type, String path) {
        return BASE_IMAGE_URL + type.name() + "/" + path;
    }
}
