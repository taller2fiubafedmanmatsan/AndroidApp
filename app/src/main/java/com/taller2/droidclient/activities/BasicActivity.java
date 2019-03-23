package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    public void changeActivity(Context from, Class to) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }
}
