package id.nerdstudio.moviecatalogue.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreferences {
    private static final String LANGUAGE = "language";

    public static void putLanguage(Context context, String value){
        saveString(context, LANGUAGE, value);
    }

    public static String getLanguage(Context context){
        return readString(context, LANGUAGE);
    }

    private static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String readString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);

    }
}
