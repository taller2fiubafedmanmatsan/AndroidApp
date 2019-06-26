package com.taller2.droidclient.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.taller2.droidclient.R;
import com.taller2.droidclient.activities.ChatActivity;
import com.taller2.droidclient.activities.MapsActivity;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.utils.GlideApp;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.taller2.droidclient.utils.GlideOptions.centerCropTransform;

public class ReceivedMapHolder extends MessageListAdapter.MyViewHolder {
    private TextView messageText, timeText, nameText;
    private ImageView image;
    private LinearLayout imageAndText;
    private TextView text;
    private View context;

    ReceivedMapHolder(View itemView) {
        super(itemView);

        context = itemView;

        //image = (ImageView) itemView.findViewById(R.id.image_message);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        text = (TextView) itemView.findViewById(R.id.image_text);
        imageAndText = (LinearLayout) itemView.findViewById(R.id.location_layout);

        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
    }

    void bind(final UserMessage message) {
        String loc_text = message.getCreator().getNickname() + " location";

        nameText.setText(message.getCreator().getNickname());
        text.setText(loc_text);

        final String[] pos = message.getMessage().split(";");
        /*String url = "http://maps.google.com/maps/api/staticmap?center="
                + pos[0]
                + ","
                + pos[1]
                + "&zoom=15&size=200x200&sensor=false"
                + "&key=" + "AIzaSyDST5GkQOOCRuMBjh9FFkj3UtOGuLhL1Eg";*/

        imageAndText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(), MapsActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", Double.valueOf(pos[0]));
                b.putDouble("lon", Double.valueOf(pos[1]));
                b.putString("name", message.getCreator().getName());
                intent.putExtras(b); //Put your id to your next Intent
                context.getContext().startActivity(intent);
            }
        });

        timeText.setText(message.getDateTime().toLocaleString());

        if (message.getPhotoUrl() == null || message.getPhotoUrl().isEmpty()) {
            GlideApp.with(context)
                    .load(context.getResources()
                            .getIdentifier("default_profile_pic", "drawable", context.getContext().getPackageName()))
                    //.centerCrop()
                    .apply(centerCropTransform())
                    .into(profileImage
                    );
        } else {
            Glide.with(context)
                    .load(message.getPhotoUrl())
                    //.centerCrop()
                    .apply(centerCropTransform())
                    .into(profileImage);
        }
        //Bitmap img = getMapImage(url);

        /*GlideApp.with(context)
                .load(url)
                //.centerCrop()
                .apply(centerCropTransform())
                .into(image);*/


    }
}