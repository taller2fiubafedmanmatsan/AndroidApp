package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.taller2.droidclient.R;

public class WorkspaceActivity extends AppCompatActivity {
    private Button button_create_workspace;
    private Button button_join_workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        button_create_workspace = findViewById(R.id.button_create_workspace);
        button_join_workspace = findViewById(R.id.button_join_workspace);

        setListeners();
    }

    private void setListeners() {

    }
}
