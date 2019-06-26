package com.taller2.droidclient.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taller2.droidclient.R;
import com.taller2.droidclient.activities.ChatActivity;
import com.taller2.droidclient.activities.MapsActivity;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.utils.GlideApp;

import static com.taller2.droidclient.utils.GlideOptions.centerCropTransform;

public class ReceivedFileHolder extends MessageListAdapter.MyViewHolder {
    private TextView messageText, timeText, nameText;
    private ImageView image;
    private LinearLayout imageAndText;
    private TextView text;
    private View context;

    ReceivedFileHolder(View itemView) {
        super(itemView);

        context = itemView;

        image = (ImageView) itemView.findViewById(R.id.file_image);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        text = (TextView) itemView.findViewById(R.id.file_text);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
    }

    void bind(final UserMessage message) {
        final String[] msg = message.getMessage().split("\\s+");

        nameText.setText(message.getCreator().getNickname());

        text.setText(msg[0]);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((ChatActivity) context.getContext()).downloadFile(msg[0], msg[1]);
                } catch (SecurityException e) {
                    Log.d("RECEIVE/FILE", e.getMessage());
                }
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
    }
}