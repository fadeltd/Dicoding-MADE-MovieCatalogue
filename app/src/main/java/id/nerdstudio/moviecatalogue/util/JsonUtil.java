package id.nerdstudio.moviecatalogue.util;

import com.google.gson.JsonObject;

public class JsonUtil {
    public static boolean isNotNull(JsonObject json, String key){
        return json.has(key) && !json.get(key).isJsonNull();
    }

    public static long getLong(JsonObject json, String key){
        return getLong(json, key, 0L);
    }

    public static long getLong(JsonObject json, String key, long defaultValue){
        return isNotNull(json, key) ? json.get(key).getAsLong() : defaultValue;
    }

    public static String getString(JsonObject json, String key) {
        return getString(json, key, "");
    }

    public static String getString(JsonObject json, String key, String defaultValue){
        return isNotNull(json, key) ? json.get(key).getAsString() : defaultValue;
    }

    public static boolean getBoolean(JsonObject json, String key){
        return getBoolean(json, key, false);
    }
    public static boolean getBoolean(JsonObject json, String key, boolean defaultValue){
        return isNotNull(json, key) ? json.get(key).getAsBoolean() : defaultValue;
    }

    public static float getFloat(JsonObject json, String key) {
        return getFloat(json, key, 0F);
    }

    public static float getFloat(JsonObject json, String key, float defaultValue){
        return isNotNull(json, key) ? json.get(key).getAsFloat() : defaultValue;
    }

    public static double getDouble(JsonObject json, String key) {
        return getDouble(json, key, 0d);
    }

    public static double getDouble(JsonObject json, String key, double defaultValue){
        return isNotNull(json, key) ? json.get(key).getAsDouble() : defaultValue;
    }
}
