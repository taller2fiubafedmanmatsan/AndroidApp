package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.droidclient.R;
import com.taller2.droidclient.utils.SavedState;

public class StartLoadingActivity extends BasicActivity {

    private ImageView loading_picture;
    private TextView loading_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);

        loading_picture = findViewById(R.id.loading_picture);
        loading_text = findViewById(R.id.loading_text);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        loading_picture.startAnimation(fadeInAnimation);

        getMyWorkspaces();
    }

    private void getMyWorkspaces() {
        //SavedState preference = new SavedState(this);

        String token = preference.getToken();

        loading_text.setText("Loading workspaces");
        //**DO REQUEST**
        //Request workspaces that im in!
        //If it fails, well, retry or logout (?)
        //If it succeeds, check if the list of workspaces is empty
        //If its empty, change activity to NoWorkspaceActivity
        //If its not empty check if actual workspace is one of those workspaces
        //If its true, change activity to ChatActivity
        //If its false, save in preference an actual workspace, choose the one you want
        //and change activity to ChatActivity

        changeActivity(StartLoadingActivity.this, NoWorkspaceActivity.class);
    }
}
