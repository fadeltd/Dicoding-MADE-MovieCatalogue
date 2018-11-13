package id.nerdstudio.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import id.nerdstudio.moviecatalogue.util.JsonUtil;

public class Movie implements Parcelable {
    private long voteCount;
    private long id;
    private boolean video;
    private float voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private int[] genreIds;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private String releaseDate;

    public Movie(long voteCount, long id, boolean video, float voteAverage, String title, double popularity, String posterPath, String originalLanguage, String originalTitle, int[] genreIds, String backdropPath, boolean adult, String overview, String releaseDate) {
        this.voteCount = voteCount;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        voteCount = in.readLong();
        id = in.readLong();
        video = in.readByte() != 0;
        voteAverage = in.readFloat();
        title = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        genreIds = in.createIntArray();
        backdropPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static Movie fromJson(JsonObject movie) {
        long voteCount = JsonUtil.getLong(movie, "vote_count");
        long id = JsonUtil.getLong(movie, "id");
        boolean video = JsonUtil.getBoolean(movie, "video");
        float voteAverage = JsonUtil.getFloat(movie, "vote_average");
        String title = JsonUtil.getString(movie, "title");
        double popularity = JsonUtil.getDouble(movie, "popularity");
        String posterPath = JsonUtil.getString(movie, "poster_path");
        String originalLanguage = JsonUtil.getString(movie, "original_language");
        String originalTitle = JsonUtil.getString(movie, "original_title");
        int[] genreIds = JsonUtil.isNotNull(movie, "genre_ids") ? new Gson().fromJson(movie.get("genre_ids"), int[].class) : new int[0];
        String backdropPath = JsonUtil.getString(movie, "backdrop_path");
        boolean adult = JsonUtil.getBoolean(movie, "adult");
        String overview = JsonUtil.getString(movie, "overview");
        String releaseDate = JsonUtil.getString(movie, "release_date");
        return new Movie(voteCount, id, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, genreIds, backdropPath, adult, overview, releaseDate);
    }

    public long getVoteCount() {
        return voteCount;
    }

    public long getId() {
        return id;
    }

    public boolean isVideo() {
        return video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(voteCount);
        parcel.writeLong(id);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeFloat(voteAverage);
        parcel.writeString(title);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeIntArray(genreIds);
        parcel.writeString(backdropPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
