package com.taller2.droidclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.utils.GlideApp;

import static com.taller2.droidclient.utils.GlideOptions.centerCropTransform;

public class ReceivedMessageHolder extends MessageListAdapter.MyViewHolder {

    TextView messageText, timeText, nameText;
    ImageView profileImage;
    private View context;

    ReceivedMessageHolder(View itemView) {
        super(itemView);

        context = itemView;

        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
    }

    void bind(UserMessage message) {
        messageText.setText(message.getMessage());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(message.getDateTime().toString());
        nameText.setText(message.getCreator().getNickname());

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
        // Insert the profile image from the URL into the ImageView.
        //Utils.displayRoundImageFromUrl(mContext, message.getSender().getPhotoUrl(), profileImage);
    }
}
