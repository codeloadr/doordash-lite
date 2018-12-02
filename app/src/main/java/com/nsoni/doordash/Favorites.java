package com.nsoni.doordash;

import android.content.Context;
import android.content.SharedPreferences;

public class Favorites {
    public static final String S_PREF = "S_PREF_FAV";

    private Favorites() {
    }

    public static boolean isFavorite(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(S_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void toggleFav(Context context, String key) {
        if (!isFavorite(context, key)) {
            saveFavorite(context, key);
        } else {
            removeFavorite(context, key);
        }
    }

    private static void saveFavorite(Context context, String key) {
        SharedPreferences.Editor e = context.getSharedPreferences(S_PREF, Context.MODE_PRIVATE).edit();
        e.putBoolean(key, true);
        e.apply();
    }

    private static void removeFavorite(Context context, String key) {
        SharedPreferences.Editor e = context.getSharedPreferences(S_PREF, Context.MODE_PRIVATE).edit();
        e.remove(key);
        e.apply();
    }
}
