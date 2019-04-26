package com.taller2.droidclient.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taller2.droidclient.R;
import com.taller2.droidclient.utils.GlideApp;

public class StartLoadingActivity extends AppCompatActivity {

    private ImageView loading_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);

        loading_picture = findViewById(R.id.loading_picture);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        loading_picture.startAnimation(fadeInAnimation);
    }
}
