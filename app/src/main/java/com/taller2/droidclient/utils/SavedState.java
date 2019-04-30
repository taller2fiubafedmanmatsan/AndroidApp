package com.taller2.droidclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;

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
        preferences.edit().putString("workspace_name", "").apply();
    }

    public WorkspaceResponse getActualWorkspace() {
        return new WorkspaceResponse(
                preferences.getString("workspace_name", "")
        );
    }

    public void saveActualWorkspace(WorkspaceResponse workspace) {
        preferences.edit().putString("workspace_name", workspace.getName()).apply();
    }

    public void saveActualChannel(Channel channel){
        preferences.edit().putString("channel_name", channel.getName()).apply();
    }

    public Channel getActualChannel(){
        return new Channel(preferences.getString("channel_name",""));
    }
}
