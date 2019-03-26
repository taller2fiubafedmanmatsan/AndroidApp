package com.taller2.droidclient.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.taller2.droidclient.R;


public class ProfileActivity extends BasicActivity{

    private EditText user_profile;
    private EditText email_profile;
    private Button button_update_profile;
    private Button button_exit;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_profile = findViewById(R.id.username_profile);
        email_profile = findViewById(R.id.email_profile);
        button_update_profile = findViewById(R.id.buttonChangeProfile);
        button_exit = findViewById(R.id.buttonExit);

        setListeners();

    }

    private void setListeners(){
        button_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_profile();
                button_update_profile.setEnabled(false);
                changeActivity(ProfileActivity.this, MainActivity.class);
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ProfileActivity.this, MainActivity.class);
            }
        });
    }


    private void update_profile() {
        String url = "https://app-server-t2.herokuapp.com/";
    }
}
