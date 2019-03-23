package com.taller2.droidclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.User;

public class ParseActivity extends BasicActivity {

    private TextView username_view;
    private TextView text_view_res;
    private Button button_parse;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);

        auth = FirebaseAuth.getInstance();

        text_view_res = findViewById(R.id.text_view_result);
        username_view = findViewById(R.id.username);
        button_parse = findViewById(R.id.button_parse);

        queue = Volley.newRequestQueue(this);

        set_button_parse_action();

        FirebaseUser user = auth.getCurrentUser();
        String userid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        //Let's listen for changes in the userid or below
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                username_view.setText(user.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void set_button_parse_action() {
        button_parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parse();
                button_parse.setEnabled(false);
            }
        });
    }

    private void Parse() {
        String url = "https://app-server-t2.herokuapp.com/";

        //Future for json
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("key");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });*/

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        text_view_res.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }
}
