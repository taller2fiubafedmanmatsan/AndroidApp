package com.taller2.droidclient.activities;

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
import com.taller2.droidclient.model.RegisterUser;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends BasicActivity {

    private EditText username_v;
    private EditText email_address_v;
    private EditText password_v;
    private EditText fullname_v;
    private Button button_register;

    private UserRequester userRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Register");

        setContentView(R.layout.activity_register);

        userRequester = new UserRequester();

        username_v = findViewById(R.id.username);
        email_address_v = findViewById(R.id.email_address);
        fullname_v = findViewById(R.id.fullname);
        password_v = findViewById(R.id.password);
        button_register = findViewById(R.id.button_register);

        setListeners();
    }
    @Override
    public void onBackPressed() {
        changeActivity(RegisterActivity.this, MainActivity.class);
    }

    private void setListeners() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = username_v.getText().toString();
                final String email_address = email_address_v.getText().toString();
                final String password = password_v.getText().toString();
                final String fullname = fullname_v.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email_address) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(username, email_address, password, fullname);
                }
            }
        });
    }

    private void registerUser(final String username, final String email, final String password, final String fullname) {
        RegisterUser user = new RegisterUser(fullname,username,email,password);
        userRequester.registerUser(user, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    loginUser(email, password);
                } else {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Log.d("LOG/Register", response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    }
                });

                call.cancel();
            }
        });
    }

    private void loginUser(String email, String password) {
        LoginUser loginuser = new LoginUser(email, password);

        userRequester.loginUser(loginuser, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String msg = response.body().string();

                if (response.isSuccessful()) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            changeActivity(RegisterActivity.this, ConfigRegisterActivity.class, msg);
                            Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "This should not occur", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Log.d("Register/Login", msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Can't enter into account. Try login in later", Toast.LENGTH_SHORT).show();
                    }
                });

                call.cancel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                changeActivity(RegisterActivity.this, MainActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
