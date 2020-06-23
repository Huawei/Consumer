package com.huawei.sinktester;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceUtil {
    public static final String PREF_FILE = "castplus";
    public static final String PREF_KEY_DISCOVERABLE = "discoverable";
    public static final String PREF_KEY_AUTH_MODE = "auth_mode";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String DEFAULT_PASSWORD = Utils.randomCode();
    private static final String TAG = "SharedPreferenceUtil";

    public static void setDiscoverable(Context context, boolean discoverable) {
        Log.d(TAG, "setDiscoverable() called, discoverable: " + discoverable);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_DISCOVERABLE, discoverable);
        editor.commit();
    }

    public static boolean getDiscoverable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        if (prefs == null) {
            setDiscoverable(context, true);
            return true;
        } else {
            return prefs.getBoolean(PREF_KEY_DISCOVERABLE, true);
        }
    }

    public static void setAuthMode(Context context, boolean passwordMode) {
        Log.d(TAG, "setAuthMode() called, passwordMode: " + passwordMode);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_AUTH_MODE, passwordMode);
        editor.commit();
    }

    public static boolean getAuthMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        if (prefs == null) {
            setAuthMode(context, false);
            return false;
        } else {
            return prefs.getBoolean(PREF_KEY_AUTH_MODE, false);
        }
    }

    public static void setPassword(Context context, String password) {
        Log.d(TAG, "setPassword() called, password: " + password);
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_PASSWORD, password);
        editor.commit();
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        String password = prefs.getString(PREF_KEY_PASSWORD, DEFAULT_PASSWORD);
        if (DEFAULT_PASSWORD.equals(password)) {
            setPassword(context, DEFAULT_PASSWORD);
        }
        return password;
    }


}
