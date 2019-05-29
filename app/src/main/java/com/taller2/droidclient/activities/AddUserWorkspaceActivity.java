package com.taller2.droidclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.Users;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AddUserWorkspaceActivity extends BasicActivity {
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
        setContentView(R.layout.activity_add_user_workspace);
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
                    AddUserWorkspaceActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddUserWorkspaceActivity.this, "Insert an email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    private void addUsers(Users users, String token){
        String work = preference.getActualWorkspace().getName();
        workspaceRequester.addUser(work,users,token, new CallbackRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    if(response.isSuccessful()){
                        AddUserWorkspaceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AddUserWorkspaceActivity.this, "User added", Toast.LENGTH_SHORT).show();
                            }
                        });
                        changeActivity(AddUserWorkspaceActivity.this, ChatActivity.class);
                    }
                }catch (Exception e){
                    Log.d("WORK/USER", e.getMessage());
                    changeActivity(AddUserWorkspaceActivity.this, ChatActivity.class);
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("WORK/USER", e.getMessage());
                call.cancel();
                changeActivity(AddUserWorkspaceActivity.this, ChatActivity.class);
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
