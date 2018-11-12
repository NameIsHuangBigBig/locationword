package com.example.locationword.locationword.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Administrator on 2017/10/17.
 */

public class PreferenceUtil {
    protected static SharedPreferences preference;
    public PreferenceUtil(Context context) {
        preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear(){
        preference.edit().clear().commit();
    }

    public String getUserId() {
        return preference.getString("userid", "");
    }

    public String getPhone() {
        return preference.getString("userphone", "");
    }
    public String getHeadImg(){
        return preference.getString("headimg","");
    }


    public void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreference(String key, int value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void savePreference(String key, long value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void savePreference(String key, String value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public long getPreference(String key) {
        return preference.getLong(key, 0);
    }

    public int getPreferenceInt(String key) {
        return preference.getInt(key, 0);
    }

    public String getValue(String key) {

        return preference.getString(key, "");
    }

    public boolean getValue2(String key) {

        return preference.getBoolean(key, false);
    }

    public void removePreference(String key) {
        SharedPreferences.Editor editor = preference.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return preference.getBoolean(key, true);
    }

}
