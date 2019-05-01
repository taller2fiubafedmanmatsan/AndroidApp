package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;
import com.taller2.droidclient.utils.SavedState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class StartLoadingActivity extends BasicActivity {

    private ImageView loading_picture;
    private TextView loading_text;

    private UserRequester userRequester;
    private List<WorkspaceResponse> workspaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);

        removeGoBackActionBar();
        changeTextActionBar("Welcome to Hypechat!");

        workspaces = new ArrayList<>();
        userRequester = new UserRequester();

        loading_picture = findViewById(R.id.loading_picture);
        loading_text = findViewById(R.id.loading_text);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        loading_picture.startAnimation(fadeInAnimation);

        sendTokenFCM();
        //getMyWorkspaces();
    }

    private void sendTokenFCM() {
        loading_text.setText("Sending data to server");
        userRequester.patchTokenFCM(preference.getToken(), preference.getActualFCM(), new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    StartLoadingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            getMyWorkspaces();
                        }
                    });
                }
                else {
                    call.cancel();
                    preference.logout();
                    changeActivity(StartLoadingActivity.this,LoginActivity.class);
                    finish();
                }

                Log.d("SendingTokenFCM", response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("SendingTokenFCM", e.getMessage());
                call.cancel();
                preference.logout();
                changeActivity(StartLoadingActivity.this,LoginActivity.class);
                finish();
            }
        });
    }

    private void getMyWorkspaces() {
        //SavedState preference = new SavedState(this);
        loading_text.setText("Loading workspaces");
        loadWorkspaces();
    }


    private void loadWorkspaces() {
       userRequester.getUser(preference.getToken(), new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();

                    final User userdata = new Gson().fromJson(msg, User.class);

                    if (response.isSuccessful()) {
                        workspaces = userdata.getWorkspaces();

                        choosePath();
                    }

                    Log.d("CreateWork/loadData", msg);

                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
                    preference.logout();
                    changeActivity(StartLoadingActivity.this,LoginActivity.class);
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CreateWork/loadData", e.getMessage());
                call.cancel();
                finish();
            }
        });
    }

    private void choosePath(){
        WorkspaceResponse work = preference.getActualWorkspace();
        if(workspaces.isEmpty()){
            changeActivity(StartLoadingActivity.this, NoWorkspaceActivity.class);
        }
        else if (workspaces.contains(work)) {
            changeActivity(StartLoadingActivity.this, ChatActivity.class);
        }else{
            preference.saveActualWorkspace(workspaces.get(0));
            changeActivity(StartLoadingActivity.this, ChatActivity.class);
        }
    }

}
