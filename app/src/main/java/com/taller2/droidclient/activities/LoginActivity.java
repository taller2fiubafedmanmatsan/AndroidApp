package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BasicActivity {

    private EditText email_address_v;
    private EditText password_v;
    private Button button_login;
    private Button button_back;
    //private FirebaseAuth auth;
    //private DatabaseReference reference;
    private UserRequester userRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRequester = new UserRequester();

        //auth = FirebaseAuth.getInstance();

        email_address_v = findViewById(R.id.email_address);
        password_v = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);
        button_back = findViewById(R.id.button_back);

        setListeners();
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
                    userRequester.getUser("5c99a05240c3d40004bdb06c", new CallbackUserRequester() {
                        @Override
                        public void onSuccess(Call call, Response response) throws IOException {
                            changeActivity(LoginActivity.this, ProfileActivity.class);

                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Log.d("LOG/Login", response.body().string());
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                }
                            });

                            call.cancel();
                        }
                    });
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(LoginActivity.this, MainActivity.class);

            }
        });
    }
}
