package com.taller2.droidclient.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.LoginUser;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BasicActivity {

    private EditText email_address_v;
    private EditText password_v;
    private Button button_login;
    private UserRequester userRequester;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Sign in");
        preferences = getSharedPreferences("login",MODE_PRIVATE);

        setContentView(R.layout.activity_login);

        userRequester = new UserRequester();

        email_address_v = findViewById(R.id.email_address);
        password_v = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);

        setListeners();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        changeActivity(LoginActivity.this, MainActivity.class);
    }
    private void setListeners() {

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_address = email_address_v.getText().toString();
                String password = password_v.getText().toString();

                if (TextUtils.isEmpty(email_address) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                } else {
                    LoginUser loginuser = new LoginUser(email_address, password);
                    userRequester.loginUser(loginuser, new CallbackUserRequester() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String msg = response.body().string();

                            if (response.isSuccessful()) {
                                preferences.edit().putBoolean("logged",true).apply();
                                preferences.edit().putString("token", msg).apply();
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        changeActivity(LoginActivity.this, ChatActivity.class, msg);
                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            Log.d("LOG/Login", msg);
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            call.cancel();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                changeActivity(LoginActivity.this, MainActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
