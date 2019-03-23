package com.taller2.droidclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends BasicActivity{

    private EditText user_profile;
    private EditText email_profile;
    private Button button_update_profile;
    private Button button_exit;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_profile = findViewById(R.id.username_profile);
        email_profile = findViewById(R.id.email_profile);
        button_update_profile = findViewById(R.id.buttonChangeProfile);
        button_exit = findViewById(R.id.buttonExit);
        queue = Volley.newRequestQueue(this);

        get_user_request();
        setListeners();

    }

    private void setListeners(){
        button_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_profile();
                button_update_profile.setEnabled(false);
                changeActivity(ProfileActivity.this, MainActivity.class);
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ProfileActivity.this, MainActivity.class);
            }
        });
    }

    private void get_user_request(){
        String url = "https://app-server-t2.herokuapp.com/";

        //Future for json
        /*
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String jsonString = response.toString();
                        User usuario = new Gson().fromJson(jsonString,User.class);
                        user_profile.setText(usuario.getUsername());
                        email_profile.setText(usuario.getUsername());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
        */
    }

    private void update_profile() {
        String url = "https://app-server-t2.herokuapp.com/";
    }
}
