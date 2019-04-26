package com.taller2.droidclient.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.RegisterUser;
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
    private Button button_recovery;
    private LoginButton loginButton;
    private UserRequester userRequester;
    private AccessToken tokenfb;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deleteActionBar();

        setContentView(R.layout.activity_main);
        userRequester = new UserRequester();

        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        button_recovery = findViewById(R.id.button_recovery);
        loginButton = findViewById(R.id.login_facebook);

        preferences = getSharedPreferences("login",MODE_PRIVATE);

        if(preferences.getBoolean("logged",false)){
            token = preferences.getString("token","");
            //changeActivity(MainActivity.this,ChatActivity.class, token);
            changeActivity(MainActivity.this,StartLoadingActivity.class, token);
        }

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
                /*final AccessToken token*/ tokenfb = loginResult.getAccessToken();

                AuthCredential credential = FacebookAuthProvider.getCredential(tokenfb.getToken());

                userRequester.facebookLogin(tokenfb.getToken(), new CallbackUserRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {

                        }

                        Log.d("Facebook/Response", response.body().string());
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Facebook/Failure", e.getMessage());
                    }
                });

                /*GraphRequest request = GraphRequest.newMeRequest(
                        token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    String fullname = object.getString("name");
                                    String email = object.getString("email");
                                    final String imageURL = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    RegisterUser user = new RegisterUser(fullname, id, email, id);

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
                        });*/

                /*Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture{url}");
                request.setParameters(parameters);
                request.executeAsync();*/
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

        button_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(MainActivity.this, RecoverPasswordActivity.class);
            }
        });
    }

}
