package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.User;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private DatabaseReference reference;

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
                changeActivityTest(MainActivity.this, RegisterActivity.class);
            }
        });

        auth = FirebaseAuth.getInstance();

        //Facebook integration
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_facebook);

        loginButton.setReadPermissions("email", "public_profile");
        // If using in a fragment
        //loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final AccessToken token = loginResult.getAccessToken();

                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

                auth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                   // FirebaseUser user = auth.getCurrentUser();

                                    //String userid = user.getUid();
                                    //updateUI(user);
                                    //reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                    //User user = new User(userid, username, "default");

                                    new GraphRequest(
                                            token,
                                            "/{name}/",
                                            null,
                                            HttpMethod.GET,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    //response.
                                                }
                                            }
                                    ).executeAsync();

                                    Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(MainActivity.this, ParseActivity.class);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);

                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Logging failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Cannot login via facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeActivityTest(Context from, Class to) {
        Intent intent = new Intent(from, to);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }
}
