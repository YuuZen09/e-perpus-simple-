package com.example.testapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.testapi.model.login.LoginData;

import java.util.HashMap;

public class SessionManager {

    private Context _context ;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USER_ID = "user_id";
    public static final String NO_ANGGOTA = "no_anggota";
    public static final String NAMA = "nama";

    public SessionManager (Context context){
        this._context = context.getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession (LoginData user){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USER_ID, user.getUserId());
        editor.putString(NO_ANGGOTA, user.getNoAnggota());
        editor.putString(NAMA, user.getName());
        editor.commit();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        user.put(NO_ANGGOTA, sharedPreferences.getString(NO_ANGGOTA, null));
        user.put(NAMA, sharedPreferences.getString(NAMA, null));
        return user;
    }

    public void logoutSession(){
        editor.clear();
        editor.commit();
    }

    public Boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
