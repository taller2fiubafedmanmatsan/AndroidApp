package com.taller2.droidclient.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;
import com.taller2.droidclient.utils.GlideApp;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ChannelActivity extends BasicActivity{

    private final int SELECT_IMAGE = 1;

    private EditText name_channel;
    private TextView welcome_channel;
    private TextView description_channel;
    private Button button_update_channel_name;

    private Channel channelData;
    private String token;
    private ChannelRequester channelRequester;
    private UserRequester userRequester;
    private LinearLayout layoutLoadingBar;

    private StorageReference mStorageRef;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Channel");

        setContentView(R.layout.activity_channel);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        preferences = getSharedPreferences("login",MODE_PRIVATE);

        name_channel = findViewById(R.id.channel_name_label);
        welcome_channel = findViewById(R.id.welcome_label);
        description_channel = findViewById(R.id.description_label);
        button_update_channel_name= findViewById(R.id.icon_edit_name_channel);
        layoutLoadingBar = findViewById(R.id.layout_progress_bar);

        token = preference.getToken();

        channelRequester = new ChannelRequester();
        userRequester = new UserRequester();

        ChannelActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                loadingSpin.showDialog(ChannelActivity.this);
            }
        });
        loadChannelData();

        setListeners();
    }

    private void loadChannelData() {
        String currentWorkspace = preference.getActualWorkspace().getName();
        String currentChannel = preference.getActualChannel().getName();

        channelRequester.getChannel(currentChannel, currentWorkspace, token, new CallbackRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    final Channel channel = new Gson().fromJson(msg, Channel.class);

                    if (response.isSuccessful()) {
                        ChannelActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                channelData = channel;
                                loadChannel();
                            }
                        });
                    }
                    Log.d("CHAN/LoadData", msg);

                }catch (Exception e){
                    Log.d("CHAN/LoadData", e.getMessage());
                    ChannelActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            loadingSpin.hideDialog();
                        }
                    });
                    changeActivity(ChannelActivity.this, ChatActivity.class);
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                ChannelActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ChannelActivity.this, "Failed to load workspaceData.", Toast.LENGTH_SHORT).show();
                        loadingSpin.hideDialog();
                        changeActivity(ChannelActivity.this, ChatActivity.class);
                    }
                });
                Log.d("CHAN/LoadData", e.getMessage());
                call.cancel();


            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void loadChannel() {
        name_channel.setText(channelData.getName());
        welcome_channel.setText(channelData.getWelcomeMessage());
        description_channel.setText(channelData.getWelcomeMessage());
        setButtons();
    }

    private void setButtons(){
        userRequester.getUser(token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final User userdata = new Gson().fromJson(msg, User.class);
                    if (response.isSuccessful()) {
                        if(!channelData.getCreator().equals(userdata)){
                            ChannelActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    button_update_channel_name.setVisibility(View.GONE);
                                    loadingSpin.hideDialog();
                                }
                            });
                        }
                    }
                    Log.d("USER/loadData", msg);
                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
                    preference.logout();
                    changeActivity(ChannelActivity.this,ChatActivity.class);
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("USER/loadData", e.getMessage());
                call.cancel();
                finish();
            }
        });
    }

    private void setListeners(){
        button_update_channel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name_channel.getText().toString();
                if(!channelData.getName().equals(newName)){
                    ChannelActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChannelActivity.this, "NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    ChannelActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChannelActivity.this, "Introduzca un nuevo nombre de workspace", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();*/
                //changeActivity(ProfileActivity.this, ChatActivity.class, token);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
