package com.taller2.droidclient.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.taller2.droidclient.R;
import com.taller2.droidclient.activities.ConfigRegisterActivity;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.utils.GlideApp;

import static com.taller2.droidclient.utils.GlideOptions.centerCropTransform;

public class ReceivedImageHolder extends MessageListAdapter.MyViewHolder {
    private TextView messageText, timeText, nameText;
    private ImageView image;
    private View context;
    private ImagePopup imagePopup;

    ReceivedImageHolder(View itemView) {
        super(itemView);

        context = itemView;

        image = (ImageView) itemView.findViewById(R.id.image_message);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);

        imagePopup = new ImagePopup(context.getContext());
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional
    }

    void bind(UserMessage message) {
        nameText.setText(message.getCreator().getNickname());

        GlideApp.with(context)
                .load(message.getMessage())
                //.centerCrop()
                .apply(centerCropTransform())
                .into(image);

            //imagePopup.initiatePopup(image.getBackground());

        imagePopup.initiatePopupWithGlide(message.getMessage());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imagePopup.initiatePopupWithGlide();
                imagePopup.viewPopup();

            }
        });
    }
}
