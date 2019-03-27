package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    public String token;

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
