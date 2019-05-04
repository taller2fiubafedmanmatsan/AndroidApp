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

public class WorkspaceActivity extends BasicActivity{

    private final int SELECT_IMAGE = 1;

    private EditText name_workspace;
    private TextView welcome_workspace;
    private TextView description_workspace;
    private Button button_update_workspace_name;
    private ImageView workspace_picture;
    private Button button_change_picture_workspace;
    private WorkspaceResponse workData;
    private String token;
    private WorkspaceRequester workspaceRequester;
    private UserRequester userRequester;
    private LinearLayout layoutLoadingBar;

    private StorageReference mStorageRef;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Workspace");

        setContentView(R.layout.activity_workspace);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        preferences = getSharedPreferences("login",MODE_PRIVATE);

        name_workspace = findViewById(R.id.workspace_name_label);
        welcome_workspace = findViewById(R.id.welcome_label);
        description_workspace = findViewById(R.id.description_label);
        button_update_workspace_name= findViewById(R.id.icon_edit_name_work);
        workspace_picture = findViewById(R.id.workspace_picture);
        button_change_picture_workspace = findViewById(R.id.icon_edit_image);
        layoutLoadingBar = findViewById(R.id.layout_progress_bar);

        token = preference.getToken();

        workspaceRequester = new WorkspaceRequester();
        userRequester = new UserRequester();

        WorkspaceActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                loadingSpin.showDialog(WorkspaceActivity.this);
            }
        });
        loadWorkspaceData();

        setListeners();
    }

    private void loadWorkspaceData() {
        String currentWorkspace = preference.getActualWorkspace().getName();
        workspaceRequester.getWorkspace(currentWorkspace, token, new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    final WorkspaceResponse workspaceData = new Gson().fromJson(msg, WorkspaceResponse.class);

                    if (response.isSuccessful()) {
                        WorkspaceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                workData = workspaceData;
                                loadWorkspace();
                            }
                        });
                    }
                    Log.d("WORK/LoadData", msg);

                }catch (Exception e){
                    Log.d("WORK/LoadData", e.getMessage());
                    WorkspaceActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            loadingSpin.hideDialog();
                        }
                    });
                    changeActivity(WorkspaceActivity.this, ChatActivity.class);
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                WorkspaceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(WorkspaceActivity.this, "Failed to load workspaceData.", Toast.LENGTH_SHORT).show();
                        loadingSpin.hideDialog();
                        changeActivity(WorkspaceActivity.this, ChatActivity.class);
                    }
                });
                Log.d("Workspace/LoadData", e.getMessage());
                call.cancel();

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void loadWorkspace() {
        name_workspace.setText(workData.getName());
        welcome_workspace.setText(workData.getWelcomeMessage());
        description_workspace.setText(workData.getDescription());

        if (workData.getImageUrl() == null || workData.getImageUrl().equals("")) {
            if (!this.isDestroyed()) {
                GlideApp.with(this)
                        .load(getResources()
                                .getIdentifier("default_profile_pic", "drawable", this.getPackageName()))
                        .centerCrop()
                        .into(workspace_picture);
            }
        } else {
            if (!this.isDestroyed()) {
                GlideApp.with(this)
                        .load(Uri.parse(workData.getImageUrl())).centerCrop().into(workspace_picture);
            }
        }
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
                        if(!workData.getAdmins().contains(userdata)){
                            WorkspaceActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    button_change_picture_workspace.setVisibility(View.GONE);
                                    button_update_workspace_name.setVisibility(View.GONE);

                                }
                            });
                        }
                        loadingSpin.hideDialog();
                    }
                    Log.d("USER/loadData", msg);
                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
                    preference.logout();
                    changeActivity(WorkspaceActivity.this,ChatActivity.class);
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
        button_update_workspace_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name_workspace.getText().toString();
                if(!workData.getName().equals(newName)){
                    WorkspaceActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WorkspaceActivity.this, "NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    WorkspaceActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WorkspaceActivity.this, "Introduzca un nuevo nombre de workspace", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        button_change_picture_workspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_gallery();
            }
        });
    }

    private void open_gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    private void changeProfilePicture(Bitmap bitmap) {
        loadingSpin.showDialog(WorkspaceActivity.this);

        String image_name = "profile-" + workData.getName() + ".jpg";
        final StorageReference mountainsRef = mStorageRef.child(image_name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(WorkspaceActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!urlTask.isSuccessful());

                final Uri downloadUrl = urlTask.getResult();

                //final Uri downloadUrl = //taskSnapshot.getDownloadUrl();

                // Cambiar foto de work
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    CropImage.activity(data.getData())
                            .start(WorkspaceActivity.this);
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(WorkspaceActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(WorkspaceActivity.this.getContentResolver(), resultUri);
                    changeProfilePicture(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
