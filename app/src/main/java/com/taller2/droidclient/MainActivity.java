package com.taller2.droidclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_login = findViewById(R.id.button_login);
        Button button_register = findViewById(R.id.button_register);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jsonParse();
                changeActivityTest(MainActivity.this, LoginActivity.class);
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jsonParse();
                changeActivityTest(MainActivity.this, LoginActivity.class);
            }
        });
    }

    private void changeActivityTest(Context from, Class to) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }
}
