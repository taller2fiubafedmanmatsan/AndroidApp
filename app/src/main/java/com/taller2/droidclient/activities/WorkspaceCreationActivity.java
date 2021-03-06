package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.NewChannel;
import com.taller2.droidclient.model.NewWorkspace;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class WorkspaceCreationActivity extends BasicActivity {
    private EditText editWorkspaceName;
    private EditText editWorkspaceWelcome;
    private EditText editWorkspaceDescription;
    private Button buttonCreateWorkspace;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;
    private ChannelRequester channelRequester;

    private String currentUserEmail;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workspace);
        token = preference.getToken();

        userRequester = new UserRequester();
        workspaceRequester = new WorkspaceRequester();
        channelRequester = new ChannelRequester();
        loadUserdata();

        this.changeTextActionBar("Create a workspace");

        buttonCreateWorkspace = findViewById(R.id.button_create_workspace);
        editWorkspaceName = findViewById(R.id.workspace_name);
        editWorkspaceWelcome = findViewById(R.id.workspace_welcome);
        editWorkspaceDescription = findViewById(R.id.workspace_description);

        setListeners();
    }

    private void setListeners() {
        buttonCreateWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String workspaceName = editWorkspaceName.getText().toString();
                final String workWelcome = editWorkspaceWelcome.getText().toString();
                final String workDescription = editWorkspaceDescription.getText().toString();

                if (!workspaceName.isEmpty() &&
                    !workWelcome.isEmpty() &&
                    !workDescription.isEmpty()) {
                    loadingSpin.showDialog(WorkspaceCreationActivity.this);
                    Workspace workspace = new Workspace(workspaceName,workWelcome,workDescription,currentUserEmail);

                    createWorkspace(workspace, token);


                } else {
                    WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WorkspaceCreationActivity.this, "Put a name for the new workspace", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void createWorkspace(Workspace workspace, String token){
        workspaceRequester.createWorkspace(workspace,token, new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final Workspace workspace = new Gson().fromJson(msg,Workspace.class);

                    if(response.isSuccessful()){
                        WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                preference.saveActualWorkspace(new WorkspaceResponse(workspace.getName()));
                                List<String> users = new ArrayList<>();
                                users.add(currentUserEmail);
                                NewChannel newChannel = new NewChannel(workspace.getName(),"General",users, "group");
                                createChannel(newChannel,preference.getToken());
                            }
                        });
                    } else {
                        WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                loadingSpin.hideDialog();
                            }
                        });
                    }
                    Log.d("CreateWork/loadData", msg);

                }catch (Exception e){
                    Log.d("CreateWork/loadData", e.getMessage());
                    WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            loadingSpin.hideDialog();
                            Toast.makeText(WorkspaceCreationActivity.this, "The workspace couldn't be created", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CreateWork/loadData", e.getMessage());
                WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                        Toast.makeText(WorkspaceCreationActivity.this, "The workspace couldn't be created", Toast.LENGTH_SHORT).show();
                    }
                });
                call.cancel();
            }
        });
    }

    private void createChannel(NewChannel channel, String token){
        String workName = preference.getActualWorkspace().getName();
        channelRequester.createChannel(channel,workName,token, new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                    }
                });
                try{
                    String msg = response.body().string();
                    Log.d("CreateWork/loadData", msg);
                    final Channel channel1 = new Gson().fromJson(msg,Channel.class);

                    if(response.isSuccessful()){
                        WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                preference.saveActualChannel(channel1);
                                //changeActivity(WorkspaceCreationActivity.this, StartLoadingActivity.class);
                                changeActivity(WorkspaceCreationActivity.this, ChatActivity.class);
                                Toast.makeText(WorkspaceCreationActivity.this, "Workspace created successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(WorkspaceCreationActivity.this, "Failed to create General channel", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }catch (Exception e){
                    Log.d("CreateWork/loadData", e.getMessage());
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                        Toast.makeText(WorkspaceCreationActivity.this, "Failed to create General channel", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("CreateWork/loadData", e.getMessage());
                call.cancel();
            }
        });
    }



    private void loadUserdata() {
        loadingSpin.showDialog(this);

        userRequester.getUser(token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                    }
                });

                try{
                    String msg = response.body().string();

                    final User newuserdata = new Gson().fromJson(msg, User.class);

                    if (response.isSuccessful()) {
                        currentUserEmail = newuserdata.getEmail();
                    }

                    Log.d("CreateWork/loadData", msg);

                }catch (Exception e){
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                    }
                });
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
