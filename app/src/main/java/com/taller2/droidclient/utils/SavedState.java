package com.taller2.droidclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.taller2.droidclient.model.Workspace;

import static android.content.Context.MODE_PRIVATE;

public class SavedState {
    private SharedPreferences preferences;
    private Context act;

    public SavedState(Context act) {
        this.act = act;
        preferences = act.getSharedPreferences("login", MODE_PRIVATE);
    }

    public boolean isLogged() {
        return preferences.getBoolean("logged", false);
    }

    public String getToken() {
        return preferences.getString("token", "");
    }

    public void saveLogin(String token) {
        preferences.edit().putBoolean("logged", true).apply();
        preferences.edit().putString("token", token).apply();
    }

    public void logout() {
        preferences.edit().putBoolean("logged", false).apply();
        preferences.edit().putString("token", "").apply();
        preferences.edit().putString("workspace", "").apply();
    }

    public String getActualWorkspace() {
        return preferences.getString("workspace", "");
    }

    public void saveActualWorkspace(Workspace workspace) {
        preferences.edit().putString("workspace", workspace.getName()).apply();
    }
}
