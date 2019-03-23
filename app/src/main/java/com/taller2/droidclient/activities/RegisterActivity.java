package com.taller2.droidclient.activities;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText username_v;
    private EditText email_address_v;
    private EditText password_v;
    private Button button_register;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        username_v = findViewById(R.id.username);
        email_address_v = findViewById(R.id.email_address);
        password_v = findViewById(R.id.password);
        button_register = findViewById(R.id.button_register);

        set_button_register_action();
    }

    private void set_button_register_action() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_v.getText().toString();
                String email_address = email_address_v.getText().toString();
                String password = password_v.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email_address) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    register(username, email_address, password);
                }
            }
        });
    }

    private void register(final String username, final String email_address, final String password) {
        //Register with firebase
        auth.createUserWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerUserInDatabase(username, email_address, password);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register failed. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUserInDatabase(String username, String email_address, String password) {
        FirebaseUser user_f = auth.getCurrentUser();
        String userid = user_f.getUid();

        User user = new User(userid, username, "default");
        //Save user in a firebase database temporary
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Register failed. Please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
