package com.taller2.droidclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;

import java.util.ArrayList;

public class WorkspaceListAdapter extends ArrayAdapter<WorkspaceResponse> {
    public WorkspaceListAdapter(Context context, ArrayList<WorkspaceResponse> workspaces) {
        super(context, 0, workspaces);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WorkspaceResponse workspace = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.format_text_navigation, parent, false);
        }

        TextView name = convertView.findViewById(android.R.id.text1);

        name.setText(workspace.getName());

        return convertView;
    }

}
