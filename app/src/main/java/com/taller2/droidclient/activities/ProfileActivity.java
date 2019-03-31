package com.taller2.droidclient.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;


public class ProfileActivity extends BasicActivity{

    private final int SELECT_IMAGE = 1;

    private EditText user_profile;
    private TextView email_profile;
    private TextView name_profile;
    private Button button_update_profile;
    private ImageView profile_picture;
    private Button button_change_password;
    private Button button_change_picture;
    private User userdata;
    private String token;
    private UserRequester userRequester;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Profile");

        setContentView(R.layout.activity_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        user_profile = findViewById(R.id.user_label);
        email_profile = findViewById(R.id.email_label);
        name_profile = findViewById(R.id.name_label);
        button_update_profile = findViewById(R.id.icon_edit_name);
        profile_picture = findViewById(R.id.profile_picture);
        button_change_password = findViewById(R.id.change_password);
        button_change_picture = findViewById(R.id.icon_edit_image);

        token = this.getUserToken();

        userRequester = new UserRequester();

        setUserNameProfile();

        Glide.with(this)
                .load("https://i.imgur.com/D0OqHFa.jpg").into(profile_picture);

        setListeners();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        changeActivity(ProfileActivity.this, MainActivity.class);
    }

    private void setUserNameProfile() {
        userRequester.getUser(token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();

                userdata = new Gson().fromJson(msg, User.class);

                if (response.isSuccessful()) {
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            user_profile.setText(userdata.getNickname());
                            email_profile.setText(userdata.getEmail());
                            name_profile.setText(userdata.getName());
                        }
                    });
                }

                Log.d("Profile/Username", msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Profile/Username", e.getMessage());
                call.cancel();
            }
        });
    }

    private void setListeners(){
        button_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNick = user_profile.getText().toString();
                if(!userdata.getNickname().equals(newNick)){
                    update_profile(newNick);
                    button_update_profile.setEnabled(false);
                }else{
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Introduzca un nuevo Nickname", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ProfileActivity.this,ChangePasswordActivity.class,token);
            }
        });

        button_change_picture.setOnClickListener(new View.OnClickListener() {
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

    private void update_profile(String newNick) {
        userRequester.changeNicknameUser(newNick, token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    changeActivity(ProfileActivity.this, ProfileActivity.class, token);

                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Invalid new password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Log.d("LOG/Change Name", response.body().string().toString());

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ERROR", e.getMessage());
                call.cancel();
            }
        });

    }

    private void changeProfilePicture(Bitmap bitmap) {
        String image_name = "profile-" + userdata.getId() + ".jpg";
        StorageReference mountainsRef = mStorageRef.child(image_name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ProfileActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                //Send to server pending

                Glide.with(ProfileActivity.this)
                        .load(downloadUrl).into(profile_picture);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                changeActivity(ProfileActivity.this, MainActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    //try {
                        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), data.getData());
                    CropImage.activity(data.getData())
                            .start(ProfileActivity.this);
                        // changeProfilePicture(bitmap);
                    /*} catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(ProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), resultUri);
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
