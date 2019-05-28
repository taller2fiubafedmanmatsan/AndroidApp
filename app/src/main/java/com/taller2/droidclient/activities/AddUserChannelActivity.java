package com.taller2.droidclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.Users;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AddUserChannelActivity extends BasicActivity {
    private EditText userEmail;
    private Button buttonAddUser;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;
    private ChannelRequester channelRequester;

    private String currentUserEmail;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_channel);
        token = preference.getToken();

        userRequester = new UserRequester();
        workspaceRequester = new WorkspaceRequester();
        channelRequester = new ChannelRequester();

        this.changeTextActionBar("Add User");

        buttonAddUser = findViewById(R.id.button_add_user);
        userEmail = findViewById(R.id.user_email);

        setListeners();
    }

    private void setListeners() {
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = userEmail.getText().toString();

                if (!user.isEmpty()) {
                    Users users = new Users(user);
                    addUsers(users, token);
                } else {
                    AddUserChannelActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddUserChannelActivity.this, "Insert an email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    private void addUsers(Users users, String token){
        String work = preference.getActualWorkspace().getName();
        String channel = preference.getActualChannel().getName();
        channelRequester.addUser(channel,work,users,token, new CallbackRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    if(response.isSuccessful()){
                        AddUserChannelActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AddUserChannelActivity.this, "User added", Toast.LENGTH_SHORT).show();
                            }
                        });
                        changeActivity(AddUserChannelActivity.this, ChatActivity.class);
                    }
                }catch (Exception e){
                    Log.d("CHANNEL/USER", e.getMessage());
                    changeActivity(AddUserChannelActivity.this, ChatActivity.class);
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CHANNEL/USER", e.getMessage());
                call.cancel();
                changeActivity(AddUserChannelActivity.this, ChatActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
