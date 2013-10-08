package com.dmitrytarianyk.dailytasks.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private static final String PREF_KEY_CALENDAR_ID = "pref_key_calendar_id";

    private static SharedPreferences mPrefs;

    public static void init(Context applicationContext) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public static void setCalendarId(long id) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(PREF_KEY_CALENDAR_ID, id);
        editor.apply();
    }

    public static long getCalendarId() {
        return mPrefs.getLong(PREF_KEY_CALENDAR_ID, 0);
    }
}
