package id.nerdstudio.moviecatalogue.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "movie_catalogue_db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s INTEGER," +
                    " %s INTEGER," +
                    " %s REAL," +
                    " %s TEXT NOT NULL," +
                    " %s REAL," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s INTEGER," +
                    " %s TEXT," +
                    " %s TEXT)",
            DatabaseContract.MovieColumns.TABLE_NAME,
            DatabaseContract.MovieColumns._ID,
            DatabaseContract.MovieColumns.VOTE_COUNT,
            DatabaseContract.MovieColumns.VIDEO,
            DatabaseContract.MovieColumns.VOTE_AVERAGE,
            DatabaseContract.MovieColumns.TITLE,
            DatabaseContract.MovieColumns.POPULARITY,
            DatabaseContract.MovieColumns.POSTER_PATH,
            DatabaseContract.MovieColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.MovieColumns.ORIGINAL_TITLE,
            DatabaseContract.MovieColumns.GENRE_IDS,
            DatabaseContract.MovieColumns.BACKDROP_PATH,
            DatabaseContract.MovieColumns.ADULT,
            DatabaseContract.MovieColumns.OVERVIEW,
            DatabaseContract.MovieColumns.RELEASE_DATE
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MovieColumns.TABLE_NAME);
        onCreate(db);
    }
}