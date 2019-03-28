package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.taller2.droidclient.R;

public class BasicActivity extends AppCompatActivity {

    public String token;
    private ActionBar top_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        top_bar = getSupportActionBar();
        setGoBackActionBar();
    }

    public void deleteTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setGoBackActionBar() {
        if (top_bar != null)
            top_bar.setDisplayHomeAsUpEnabled(true);
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

    public String getUserToken(){
        Bundle bundle = getIntent().getExtras();
        String userToken = bundle.getString("userToken");
        return userToken;
    }
}
