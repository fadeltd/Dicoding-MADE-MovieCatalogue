package id.nerdstudio.moviecatalogue.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "id.nerdstudio.moviecatalogue";
    private static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static final class MovieColumns implements BaseColumns {
        public static final String TABLE_NAME = "table_movie";
        public static final String VOTE_COUNT = "vote_count";
        public static final String VIDEO = "video";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "poster_path";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String GENRE_IDS = "genre_ids";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String ADULT = "adult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }

    public static boolean getColumnBoolean(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName)) == 1; // 1 true, 0 false
    }
}
