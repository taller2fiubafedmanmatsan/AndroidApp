package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.requesters.UserRequester;

public class ChannelCreationActivity extends BasicActivity {
    private EditText channelName;
    private Button buttonCreateChannel;
    private String token;
    private UserRequester userRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Channel creation");

        setContentView(R.layout.activity_channel_creation);

        channelName = findViewById(R.id.channel_name);
        buttonCreateChannel = findViewById(R.id.button_create_channel);

        token = preference.getToken();//this.getUserToken();

        userRequester = new UserRequester();

        setListeners();
    }

    @Override
    public void onBackPressed() {
        changeActivity(ChannelCreationActivity.this, ChatActivity.class, token);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();*/
                changeActivity(ChannelCreationActivity.this, ChatActivity.class, token);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners(){
        buttonCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channel = channelName.getText().toString();

                if (!channel.isEmpty()) {
                    //**DO REQUEST**
                    //Request for creating a channel and then change to ChatActivity
                    ChannelCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChannelCreationActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                            changeActivity(ChannelCreationActivity.this, ChatActivity.class, token);
                        }
                    });
                } else {
                    ChannelCreationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChannelCreationActivity.this, "Insert a name for the new channel", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
