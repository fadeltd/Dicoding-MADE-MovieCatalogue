package id.nerdstudio.moviecatalogue.util;

import com.google.gson.JsonObject;

public class JsonUtil {
    public static boolean isNotNull(String key, JsonObject json){
        return json.has(key) && !json.get(key).isJsonNull();
    }
}
