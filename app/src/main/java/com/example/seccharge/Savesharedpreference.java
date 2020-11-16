package com.example.seccharge;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class Savesharedpreference {
    private static final String PREF_USER_NAME= "UserDetails";
    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void setUserDetails(Context ctx, Authinfo detail) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        editor.putString(PREF_USER_NAME, json);
        editor.apply();
    }

    public static Authinfo getUserDetails(Context ctx) {
        String json =  getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
        Gson gson = new Gson();
        return gson.fromJson(json, Authinfo.class);
    }

    public static void clearUserDetails(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_NAME); //clear all stored data
        editor.apply();
    }
}
