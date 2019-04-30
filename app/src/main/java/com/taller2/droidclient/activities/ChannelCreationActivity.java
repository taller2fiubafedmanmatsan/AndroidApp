package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ChannelCreationActivity extends BasicActivity {
    private EditText channelName;
    private Button buttonCreateChannel;
    private String token;
    private UserRequester userRequester;
    private ChannelRequester channelRequester;

    private String currentUserEmail;
    private String currentWorkspace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Channel creation");

        setContentView(R.layout.activity_channel_creation);

        channelName = findViewById(R.id.channel_name);
        buttonCreateChannel = findViewById(R.id.button_create_channel);

        token = preference.getToken();//this.getUserToken();
        currentWorkspace = preference.getActualWorkspace().getName();

        userRequester = new UserRequester();
        channelRequester = new ChannelRequester();
        loadUserdata();

        setListeners();
    }

    @Override
    public void onBackPressed() {
        changeActivity(ChannelCreationActivity.this, ChatActivity.class, token);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();*/
                changeActivity(ChannelCreationActivity.this, ChatActivity.class, token);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners(){
        buttonCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channel = channelName.getText().toString();

                if (!channel.isEmpty()) {
                    List<String> users = new ArrayList<>();
                    users.add(currentUserEmail);
                    NewChannel newChannel = new NewChannel(currentWorkspace,channel,users);

                    createChannel(newChannel,token);

                } else {
                    ChannelCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChannelCreationActivity.this, "Insert a name for the new channel", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



    }

    private void createChannel(NewChannel channel, String token){
        String workName = preference.getActualWorkspace().getName();
        channelRequester.createChannel(channel,workName,token, new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final Channel channel1 = new Gson().fromJson(msg,Channel.class);

                    if(response.isSuccessful()){

                        ChannelCreationActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                changeActivity(ChannelCreationActivity.this, StartLoadingActivity.class);
                            }
                        });
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
}
