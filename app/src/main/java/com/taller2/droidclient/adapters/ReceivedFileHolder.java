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

import com.taller2.droidclient.R;
import com.taller2.droidclient.activities.ChatActivity;
import com.taller2.droidclient.activities.MapsActivity;
import com.taller2.droidclient.model.UserMessage;

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
    }
}