package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taller2.droidclient.R;

public class NoWorkspaceActivity extends BasicActivity {
    private Button button_create_workspace;
    private Button button_join_workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_workspace);

        this.removeGoBackActionBar();
        this.changeTextActionBar("Welcome to Hypechat!");

        button_create_workspace = findViewById(R.id.button_create_workspace);
        button_join_workspace = findViewById(R.id.button_join_workspace);

        setListeners();
    }

    private void setListeners() {
        button_create_workspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivityNotFinish(NoWorkspaceActivity.this, WorkspaceCreationActivity.class);
            }
        });

        button_join_workspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        changeActivityNotFinish(NoWorkspaceActivity.this, WorkspaceJoinActivity.class);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                changeActivityNotFinish(NoWorkspaceActivity.this,ProfileActivity.class);
                return true;
            case R.id.action_logout:
                preference.logout();
                changeActivity(NoWorkspaceActivity.this,MainActivity.class);
                Toast.makeText(NoWorkspaceActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
