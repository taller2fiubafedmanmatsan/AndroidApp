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

    private EditText user_profile;
    private TextView email_profile;
    private TextView name_profile;
    private Button button_update_profile;
    private ImageView profile_picture;
    private Button button_change_password;
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
        name_profile = findViewById(R.id.name_label);
        button_update_profile = findViewById(R.id.icon_edit_name);
        profile_picture = findViewById(R.id.profile_picture);
        button_change_password = findViewById(R.id.change_password);

        token = this.getUserToken();

        userRequester = new UserRequester();

        setUserNameProfile();

        Glide.with(this)
                .load("https://i.imgur.com/D0OqHFa.jpg").into(profile_picture);

        setListeners();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        changeActivity(ProfileActivity.this, MainActivity.class);
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
                            user_profile.setText(userdata.getNickname());
                            email_profile.setText(userdata.getEmail());
                            name_profile.setText(userdata.getName());
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
                String newNick = user_profile.getText().toString();
                if(!userdata.getNickname().equals(newNick)){
                    update_profile(newNick);
                    button_update_profile.setEnabled(false);
                }else{
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Introduzca un nuevo Nickname", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ProfileActivity.this,ChangePasswordActivity.class,token);
            }
        });
    }


    private void update_profile(String newNick) {
        userRequester.changeNicknameUser(newNick, token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    changeActivity(ProfileActivity.this, ProfileActivity.class, token);

                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Invalid new password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Log.d("LOG/Change Name", response.body().string().toString());

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ERROR", e.getMessage());
                call.cancel();
            }
        });

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
