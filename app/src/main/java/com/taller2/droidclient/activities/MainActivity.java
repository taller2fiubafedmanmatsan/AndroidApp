package com.taller2.droidclient.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.utils.SavedState;

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
    //SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deleteActionBar();

        setContentView(R.layout.activity_main);

        createNotificationChannel();

        userRequester = new UserRequester();

        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        button_recovery = findViewById(R.id.button_recovery);
        loginButton = findViewById(R.id.login_facebook);

        /*if (Build.VERSION.SDK_INT >= 23)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);*/
        /*preferences = getSharedPreferences("login",MODE_PRIVATE);

        if(preferences.getBoolean("logged",false)){
            token = preferences.getString("token","");
            //changeActivity(MainActivity.this,ChatActivity.class, token);
            changeActivity(MainActivity.this,StartLoadingActivity.class, token);
        }*/

        //FirebaseMessaging.getInstance();
        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Remove InstanceID initiate to unsubscribe all topic
                    // TODO: May be a better way to use FirebaseMessaging.getInstance().unsubscribeFromTopic()
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            finish();
        }*/
        //FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        //Log.d("FIREBASE/MSG/TEST", FirebaseMessaging.getInstance().toString());
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        if (preference.isLogged()) {
            //sendActualToken();
            Log.d("INITIAL", preference.getActualChannel().getName());
            changeActivity(MainActivity.this,StartLoadingActivity.class);
        }

        setListeners();
        setCallbacksFacebook();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.default_notification_channel_id);
            String description = getString(R.string.default_notification_channel_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

                //AuthCredential credential = FacebookAuthProvider.getCredential(tokenfb.getToken());

                userRequester.facebookLogin(tokenfb.getToken(), new CallbackUserRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String msg = response.body().string();

                        if (response.isSuccessful()) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    preference.saveLogin(msg);
                                    changeActivity(MainActivity.this, StartLoadingActivity.class, msg);
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            LoginManager.getInstance().logOut();
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        Log.d("Facebook/Response", msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Facebook/Failure", e.getMessage());
                        LoginManager.getInstance().logOut();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        });                    }
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
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
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
