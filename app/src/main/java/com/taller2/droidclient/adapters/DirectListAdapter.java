package com.taller2.droidclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.Channel;

import java.util.ArrayList;

public class DirectListAdapter extends ArrayAdapter<Channel> {
    public DirectListAdapter(Context context, ArrayList<Channel> channels) {
        super(context, 0, channels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Channel channel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_text_navigation, parent, false);
        }

        TextView name = convertView.findViewById(android.R.id.text1);

        name.setText(channel.getFakeName());

        return convertView;
    }
}
