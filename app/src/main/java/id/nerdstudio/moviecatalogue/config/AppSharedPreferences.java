package id.nerdstudio.moviecatalogue.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreferences {
    private static final String LANGUAGE = "language";
    private static final String REMINDER = "reminder";

    public static void putLanguage(Context context, String value){
        saveString(context, LANGUAGE, value);
    }

    public static void putReminder(Context context, boolean value){
        saveBoolean(context, REMINDER, value);
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

    private static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static String readString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);

    }

    public static boolean getReminderEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(REMINDER, true);
    }
}
