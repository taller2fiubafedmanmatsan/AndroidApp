package com.taller2.droidclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.requesters.UserRequester;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ConfigRegisterActivity extends BasicActivity {

    private final int SELECT_IMAGE = 1;

    private Button button_omit;
    private Button button_finish;
    private ImageView profile_picture;
    private Button button_change_picture;

    private String token;

    private UserRequester userRequester;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Profile configuration");
        removeGoBackActionBar();

        setContentView(R.layout.activity_config_register);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        profile_picture = findViewById(R.id.profile_picture);
        button_change_picture = findViewById(R.id.icon_edit_image);
        button_omit = findViewById(R.id.button_skip_profile);
        button_finish = findViewById(R.id.button_finish_profile);

        button_finish.setEnabled(false);
        button_finish.getBackground().setAlpha(64);

        token = preference.getToken();//this.getUserToken();

        userRequester = new UserRequester();

        if (!this.isDestroyed()) {
            Glide.with(this)
                    .load(getResources()
                            .getIdentifier("default_profile_pic", "drawable", this.getPackageName()))
                    .centerCrop()
                    .into(profile_picture);
        }
        setListeners();
    }

    @Override
    public void onBackPressed() {
        //Its like omitting this step
        changeActivity(ConfigRegisterActivity.this, StartLoadingActivity.class, token);
    }

    private void setListeners(){
        button_change_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_gallery();
            }
        });

        button_omit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ConfigRegisterActivity.this, StartLoadingActivity.class, token);
            }
        });

        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ConfigRegisterActivity.this, StartLoadingActivity.class, token);
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
        loadingSpin.showDialog(ConfigRegisterActivity.this);

        final String image_name = "profile-" + "random" + ".jpg";
        StorageReference mountainsRef = mStorageRef.child(image_name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ConfigRegisterActivity.this, "Image failed to load: Try again", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!urlTask.isSuccessful());

                final Uri downloadUrl = urlTask.getResult();
                //final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                //Tener en cuenta que pasaría si al cambiar la imagen nunca se envía al server el
                //link (tecnicamente la sube pero nunca actualiza asi que el servidor nunca se entera)
                userRequester.changeProfilePic(downloadUrl, token, new CallbackUserRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("Changing/ProfilePic", response.body().string());
                        ConfigRegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                loadingSpin.hideDialog();
                            }
                        });
                        //userdata.setPhotoUrl(downloadUrl.toString());
                        if (response.isSuccessful()) {
                            ConfigRegisterActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    button_finish.setEnabled(true);
                                    button_finish.getBackground().setAlpha(0);
                                    if (!ConfigRegisterActivity.this.isDestroyed()) {
                                        Glide.with(ConfigRegisterActivity.this)
                                                .load(downloadUrl).centerCrop().into(profile_picture);
                                    }
                                }
                            });
                        } else {
                            ConfigRegisterActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ConfigRegisterActivity.this, "Image failed to load: Try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Changing/ProfilePic", e.getMessage());
                        ConfigRegisterActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                loadingSpin.hideDialog();
                                Toast.makeText(ConfigRegisterActivity.this, "Image failed to load: Try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    CropImage.activity(data.getData())
                            .start(ConfigRegisterActivity.this);
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(ConfigRegisterActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ConfigRegisterActivity.this.getContentResolver(), resultUri);
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
