package com.taller2.droidclient.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import com.google.gson.Gson;

import org.json.JSONObject;


public class ProfileActivity extends BasicActivity{

    private TextView user_profile;
    private TextView email_profile;
    private Button button_update_profile;
    private ImageView profile_picture;
    //private Button button_exit;
    private User userdata;
    private String token;
    private UserRequester userRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Profile");

        setContentView(R.layout.activity_profile);

        user_profile = findViewById(R.id.user_label);
        email_profile = findViewById(R.id.email_label);
        button_update_profile = findViewById(R.id.icon_edit_name);
        profile_picture = findViewById(R.id.profile_picture);

        token = this.getUserToken();

        userRequester = new UserRequester();

        setUserNameProfile();

        Glide.with(this)
                .load("https://i.imgur.com/D0OqHFa.jpg").into(profile_picture);

        setListeners();

    }

    private void setUserNameProfile() {
        userRequester.getUser(token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();

                userdata = new Gson().fromJson(msg, User.class);

                if (response.isSuccessful()) {
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            user_profile.setText(userdata.getName());
                            Log.d("Profile/Username", userdata.getName());
                        }
                    });
                }

                Log.d("Profile/Username", msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Profile/Username", e.getMessage());
                call.cancel();
            }
        });
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

        /*button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ProfileActivity.this, MainActivity.class);
            }
        });*/
    }


    private void update_profile() {
        String url = "https://app-server-t2.herokuapp.com/";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                changeActivity(ProfileActivity.this, MainActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
