package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.Workspace;

public class WorkspaceCreationActivity extends BasicActivity {
    private EditText editWorkspaceName;
    private Button buttonCreateWorkspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workspace);

        this.changeTextActionBar("Create a workspace");

        buttonCreateWorkspace = findViewById(R.id.button_create_workspace);
        editWorkspaceName = findViewById(R.id.workspace_name);

        setListeners();
    }

    private void setListeners() {
        buttonCreateWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String workspaceName = editWorkspaceName.getText().toString();

                if (!workspaceName.isEmpty()) {
                    String token = preference.getToken();

                    //**DO REQUEST**
                    //Request for creating a workspace
                    //If it ask for a list of users, for now, send an empty list
                    //If the request completes successfully, change preferences to set
                    //actual workspace to that workspace and change activity to ChatActivity, zip zap finish

                    WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Workspace new_workspace = new Workspace("1", workspaceName);

                            preference.saveActualWorkspace(new_workspace);

                            changeActivity(WorkspaceCreationActivity.this, ChatActivity.class);
                            Toast.makeText(WorkspaceCreationActivity.this, "Workspace created successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    WorkspaceCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WorkspaceCreationActivity.this, "Insert a name for the new workspace", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
