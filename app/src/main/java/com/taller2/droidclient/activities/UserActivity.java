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
import com.taller2.droidclient.model.Admins;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;
import com.taller2.droidclient.utils.GlideApp;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class UserActivity extends BasicActivity{

    private final int SELECT_IMAGE = 1;

    private TextView user_profile;
    private TextView email_profile;
    private TextView name_profile;
    private ImageView profile_picture;
    private User userdata;
    private String token;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;
    private LinearLayout layoutLoadingBar;
    private Button button_make_admin;
    private Button button_remove_admin;

    private StorageReference mStorageRef;
    private SharedPreferences preferences;

    private String userEmail;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("User Profile");

        setContentView(R.layout.activity_user);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        preferences = getSharedPreferences("login",MODE_PRIVATE);

        user_profile = findViewById(R.id.user_label);
        email_profile = findViewById(R.id.email_label);
        name_profile = findViewById(R.id.name_label);
        profile_picture = findViewById(R.id.profile_picture);
        layoutLoadingBar = findViewById(R.id.layout_progress_bar);
        button_make_admin = findViewById(R.id.add_admin);
        button_remove_admin = findViewById(R.id.remove_admin);

        token = preference.getToken();
        userEmail = getIntent().getStringExtra("userToken");
        currentEmail = getIntent().getStringExtra("currentEmail");


        userRequester = new UserRequester();
        workspaceRequester = new WorkspaceRequester();



        UserActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                loadingSpin.showDialog(UserActivity.this);
            }
        });
        loadUserdata();
        setListeners();
    }

    private void setListeners(){
        button_make_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String work = preference.getActualWorkspace().getName();
                String token = preference.getToken();
                Admins admin = new Admins(userEmail);

                workspaceRequester.addAdmin(work, admin, token, new CallbackRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            String msg = response.body().string();
                            if (response.isSuccessful()) {
                                UserActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(UserActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                                        changeActivity(UserActivity.this, ChatActivity.class);
                                    }
                                });
                            }
                            Log.d("ADMIN/CHANGE", msg);

                        }catch (Exception e){
                            Log.d("ADMIN/CHANGE", e.getMessage());
                            UserActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(UserActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                                    changeActivity(UserActivity.this, ChatActivity.class);
                                }
                            });
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(UserActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                                changeActivity(UserActivity.this, ChatActivity.class);
                            }
                        });
                        Log.d("ADMIN/CHANGE", e.getMessage());
                        call.cancel();

                    }
                });
            }
        });


        button_remove_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String work = preference.getActualWorkspace().getName();
                String token = preference.getToken();
                Admins admin = new Admins(userEmail);

                workspaceRequester.removeAdmin(work, admin, token, new CallbackRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            String msg = response.body().string();
                            if (response.isSuccessful()) {
                                UserActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(UserActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                                        changeActivity(UserActivity.this, ChatActivity.class);
                                    }
                                });
                            }
                            Log.d("ADMIN/CHANGE", msg);

                        }catch (Exception e){
                            Log.d("ADMIN/CHANGE", e.getMessage());
                            UserActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(UserActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                                    changeActivity(UserActivity.this, ChatActivity.class);
                                }
                            });
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(UserActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                                changeActivity(UserActivity.this, ChatActivity.class);
                            }
                        });
                        Log.d("ADMIN/CHANGE", e.getMessage());
                        call.cancel();

                    }
                });
            }
        });
    }

    private void loadUserdata() {

        userRequester.getOtherUser(userEmail,token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    final User newuserdata = new Gson().fromJson(msg, User.class);
                    if (response.isSuccessful()) {
                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                userdata = newuserdata;
                                reloadProfile();
                                getWorkspace();

                            }
                        });
                    }

                    Log.d("Profile/ReloadData", msg);
                }catch (Exception e){
                    UserActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            loadingSpin.hideDialog();
                        }
                    });
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                UserActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UserActivity.this, "Failed to reload profile.", Toast.LENGTH_SHORT).show();
                        loadingSpin.hideDialog();
                    }
                });

                Log.d("Profile/ReloadData", e.getMessage());
                call.cancel();
            }
        });
    }

    private void getWorkspace(){
        String work = preference.getActualWorkspace().getName();
        String token = preference.getToken();
        workspaceRequester.getWorkspace(work, token, new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final WorkspaceResponse workspaceData = new Gson().fromJson(msg, WorkspaceResponse.class);
                    if (response.isSuccessful()) {
                        boolean userIsAdmin = false;
                        boolean currentIsAdmin = false;
                        for (User user: workspaceData.getAdmins()) {
                            if (user.getEmail().equals(userEmail)){
                                userIsAdmin = true;
                            }
                            if(user.getEmail().equals(currentEmail)){
                                currentIsAdmin = true;
                            }
                        }
                        setButtons(userIsAdmin,currentIsAdmin);

                    }
                    Log.d("USER/PRIVI", msg);
                    loadingSpin.hideDialog();

                }catch (Exception e){
                    Log.d("USER/PRIVI", e.getMessage());
                    changeActivity(UserActivity.this, ChatActivity.class);
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("USER/PRIVI", e.getMessage());
                changeActivity(UserActivity.this, ChatActivity.class);
                finish();
            }
        });

    }

    private void setButtons(final boolean is_admin, final boolean currentAdmin){
        UserActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(!currentAdmin){
                    button_make_admin.setVisibility(View.GONE);
                    button_remove_admin.setVisibility(View.GONE);
                }
                if(currentAdmin && !is_admin){
                    button_remove_admin.setVisibility(View.GONE);
                }
                if(currentAdmin && is_admin){
                    button_make_admin.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void reloadProfile() {
        user_profile.setText(userdata.getNickname());
        email_profile.setText(userdata.getEmail());
        name_profile.setText(userdata.getName());

        if (userdata.getPhotoUrl() == null || userdata.getPhotoUrl().equals("")) {
            if (!this.isDestroyed()) {
                GlideApp.with(this)
                        .load(getResources()
                                .getIdentifier("default_profile_pic", "drawable", this.getPackageName()))
                        .centerCrop()
                        .into(profile_picture);
            }
        } else {
            if (!this.isDestroyed()) {
                GlideApp.with(this)
                        .load(Uri.parse(userdata.getPhotoUrl())).centerCrop().into(profile_picture);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
