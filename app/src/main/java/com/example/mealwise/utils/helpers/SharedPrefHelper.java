package com.example.mealwise.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private static final String PREF_NAME = "MealWisePrefs";
    private static final String KEY_IS_FIRST_TIME = "IsFirstTime";
    private static final String KEY_IS_GUEST = "IsGuest";

    private static SharedPrefHelper instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefHelper(context);
        }
        return instance;
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_TIME, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_FIRST_TIME, isFirstTime);
        editor.apply();
    }

    public boolean isGuestMode() {
        return sharedPreferences.getBoolean(KEY_IS_GUEST, false);
    }

    public void setGuestMode(boolean isGuest) {
        editor.putBoolean(KEY_IS_GUEST, isGuest);
        editor.apply();
    }

    public void clearPreferences() {
        boolean isFirstTime = isFirstTimeLaunch();

        editor.clear();
        editor.apply();
        setFirstTimeLaunch(isFirstTime);
    }
}