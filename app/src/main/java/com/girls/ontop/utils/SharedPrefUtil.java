package com.girls.ontop.utils;

import android.content.SharedPreferences;

public class SharedPrefUtil {

    private SharedPreferences sharedPreferences;


    public SharedPrefUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean setValueForKey(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            return false;
        }

        editor.apply();
        return true;
    }

    public String getStringValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getIntValue(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public float getFloatValue(String key) {
        return sharedPreferences.getFloat(key, -1.0f);
    }

    public long getLongValue(String key) {
        return sharedPreferences.getLong(key, -1L);
    }
}