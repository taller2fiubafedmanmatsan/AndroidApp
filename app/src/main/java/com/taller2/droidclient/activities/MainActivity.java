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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BasicActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Button button_login;
    private Button button_register;
    private Button button_exit;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        button_exit = findViewById(R.id.button_exit);
        loginButton = findViewById(R.id.login_facebook);

        setListeners();
        setCallbacks();
    }

    private void setCallbacks() {
        //Facebook integration
        callbackManager = CallbackManager.Factory.create();

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
                                    GraphRequest request = GraphRequest.newMeRequest(
                                            token,
                                            new GraphRequest.GraphJSONObjectCallback() {
                                                @Override
                                                public void onCompleted(JSONObject object, GraphResponse response) {
                                                    try {
                                                        String id = object.getString("id");
                                                        String name = object.getString("name");
                                                        String email = object.getString("email");
                                                        final String imageURL = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                                        //Temporary use firebase database
                                                        FirebaseUser user_f = auth.getCurrentUser();

                                                        String userid = user_f.getUid();
                                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                                        User user = new User(userid, name, "default");

                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    changeActivity(MainActivity.this, ParseActivity.class);
                                                                    Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(MainActivity.this, "Logging failed. Please try again",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        //Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();

                                                        //changeActivity(MainActivity.this, ParseActivity.class);
                                                    } catch (JSONException e) {
                                                        Toast.makeText(MainActivity.this, "Logging failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email,picture{url}");
                                    request.setParameters(parameters);
                                    request.executeAsync();
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

    private void setListeners(){
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(MainActivity.this, LoginActivity.class);
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(MainActivity.this, RegisterActivity.class);
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

    }

}
