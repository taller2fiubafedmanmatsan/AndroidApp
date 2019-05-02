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
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.NewChannel;
import com.taller2.droidclient.model.User;
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

public class WorkspaceJoinActivity extends BasicActivity {
    private EditText editWorkspaceName;
    private Button buttonJoinWorkspace;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;
    private ChannelRequester channelRequester;

    private String currentUserEmail;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_workspace);
        token = preference.getToken();

        userRequester = new UserRequester();
        workspaceRequester = new WorkspaceRequester();
        channelRequester = new ChannelRequester();
        loadUserdata();

        this.changeTextActionBar("Join a workspace");

        buttonJoinWorkspace = findViewById(R.id.button_join_workspace);
        editWorkspaceName = findViewById(R.id.workspace_name);

        setListeners();
    }

    private void setListeners() {
        buttonJoinWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String workspaceName = editWorkspaceName.getText().toString();

                if (!workspaceName.isEmpty()) {
                    Workspace workspace = new Workspace(workspaceName,currentUserEmail);
                    joinWorkspace(workspace, token);


                } else {
                    WorkspaceJoinActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WorkspaceJoinActivity.this, "Insert a name from an existing workspace", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void joinWorkspace(Workspace workspace, String token){
        workspaceRequester.joinWorkspace(workspace,token, new CallbackRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final Workspace workspace = new Gson().fromJson(msg,Workspace.class);

                    if(response.isSuccessful()){
                        WorkspaceJoinActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                preference.saveActualWorkspace(new WorkspaceResponse(workspace.getName()));
                                joinGeneralChannel(workspace.getName());

                            }
                        });
                    }
                    Log.d("JOINWORK", msg);

                }catch (Exception e){
                    Log.d("JOINWORK", e.getMessage());
                    loadingSpin.hideDialog();
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("JOINWORK", e.getMessage());
                call.cancel();
            }
        });
    }

    private void joinGeneralChannel(String workName){
        List<String> users = new ArrayList<>();
        users.add(currentUserEmail);
        channelRequester.joinChannel(workName, users, preference.getToken(), new CallbackRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    if(response.isSuccessful()){
                        WorkspaceJoinActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                preference.saveActualChannel(new Channel("General"));
                                changeActivity(WorkspaceJoinActivity.this, StartLoadingActivity.class);
                                Toast.makeText(WorkspaceJoinActivity.this, "Join Succefull", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Log.d("JOINCHANNEL", msg);

                }catch (Exception e){
                    Log.d("JOINCHANNEL", e.getMessage());
                    loadingSpin.hideDialog();
                    finish();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("JOINCHANNEL", e.getMessage());
                call.cancel();


            }
        });

    }

    private void loadUserdata() {
        loadingSpin.showDialog(this);

        userRequester.getUser(token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    final User newuserdata = new Gson().fromJson(msg, User.class);

                    if (response.isSuccessful()) {
                        currentUserEmail = newuserdata.getEmail();
                        loadingSpin.hideDialog();
                    }

                    Log.d("CreateWork/loadData", msg);

                }catch (Exception e){
                    loadingSpin.hideDialog();
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CreateWork/loadData", e.getMessage());
                call.cancel();
                onBackPressed();
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
