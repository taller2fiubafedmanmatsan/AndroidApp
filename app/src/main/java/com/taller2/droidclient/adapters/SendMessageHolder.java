package com.taller2.droidclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.UserMessage;

public class SendMessageHolder extends MessageListAdapter.MyViewHolder {

    TextView messageText, timeText;

    SendMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
    }

    void bind(UserMessage message) {
        messageText.setText(message.getMessage());

        // Format the stored timestamp into a readable String using method.
        //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

    }
}
