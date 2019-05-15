package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.github.tutorialsandroid.filepicker.model.DialogConfigs;
import com.github.tutorialsandroid.filepicker.model.DialogProperties;
import com.google.gson.Gson;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.utils.LoadingSpin;
import com.taller2.droidclient.utils.SavedState;

import java.io.File;

public class BasicActivity extends AppCompatActivity {
    private ActionBar top_bar;
    protected SavedState preference;
    protected LoadingSpin loadingSpin;
    protected final int SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        top_bar = getSupportActionBar();
        setGoBackActionBar();

        preference = new SavedState(this);
        loadingSpin = new LoadingSpin();
    }

    public void deleteTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setGoBackActionBar() {
        if (top_bar != null)
            top_bar.setDisplayHomeAsUpEnabled(true);
    }

    public void removeGoBackActionBar() {
        if (top_bar != null)
            top_bar.setDisplayHomeAsUpEnabled(false);
    }

    public void changeTextActionBar(String msg) {
        top_bar.setTitle(msg);
    }

    public void deleteActionBar() {
        if (top_bar != null)
            top_bar.hide();
    }

    public void changeActivity(Context from, Class to) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }

    public void changeActivity(Context from, Class to, String token) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userToken",token);

        startActivity(intent);

        finish();
    }

    public void changeActivityNotFinish(Context from, Class to, String token) {
        Intent intent = new Intent(from, to);
        intent.putExtra("userToken",token);
        startActivity(intent);
    }

    public void changeActivityNotFinish(Context from, Class to) {
        Intent intent = new Intent(from, to);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        //finish();
    }

    public void changeActivity(Context from, Class to, String token, String data) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userToken",token);
        intent.putExtra("userdata",data);

        startActivity(intent);

        finish();
    }

    public void changeActivity(Context from, Class to, String token, User userdata) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userToken",token);
        intent.putExtra("userdata", new Gson().toJson(userdata));

        startActivity(intent);

        finish();
    }

    protected void open_gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }
}
