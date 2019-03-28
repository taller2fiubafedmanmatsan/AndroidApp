package com.taller2.droidclient.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
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
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.RegisterUser;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.requesters.UserRequester;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BasicActivity {

    private CallbackManager callbackManager;
    private Button button_login;
    private Button button_register;
    private LoginButton loginButton;
    private UserRequester userRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deleteActionBar();

        setContentView(R.layout.activity_main);
        userRequester = new UserRequester();

        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        loginButton = findViewById(R.id.login_facebook);

        setListeners();
        setCallbacksFacebook();
    }

    private void setCallbacksFacebook() {
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

                GraphRequest request = GraphRequest.newMeRequest(
                        token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    String fullname = object.getString("name");
                                    String email = object.getString("email");
                                    final String imageURL = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    RegisterUser user = new RegisterUser(fullname, id, email, id, true);

                                    userRequester.registerUser(user, new CallbackUserRequester() {
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String msg = response.body().string();

                                            if (response.isSuccessful()) {
                                                changeActivity(MainActivity.this, ProfileActivity.class, msg);

                                                MainActivity.this.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                //Handle already registered
                                                MainActivity.this.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(MainActivity.this, "There's already an account with that email", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                //Log out...
                                                LoginManager.getInstance().logOut();
                                            }

                                            Log.d("LOG/Register", msg);
                                        }

                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.d("LOG/Register", e.getMessage());
                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(MainActivity.this, "Facebook authentication failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            call.cancel();
                                        }
                                    });
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "Facebook authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture{url}");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
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
    }

}
