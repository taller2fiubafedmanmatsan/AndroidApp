package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.NewChannel;
import com.taller2.droidclient.requesters.ChannelRequester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AddDirectChannelActivity extends BasicActivity {
    private ArrayList<String> emails;
    private String myEmail;
    private ListView listEmails;
    private ChannelRequester channelRequester;

    private final String DESCRIPTION = "Direct message with user";
    private final String WELCOME_MESSAGE = "Say hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTextActionBar("Create a conversation");

        setContentView(R.layout.activity_add_direct_channel);

        listEmails = findViewById(R.id.list_users);

        channelRequester = new ChannelRequester();

        Bundle b = getIntent().getExtras();

        if (b != null) {
            emails = b.getStringArrayList("emails");
            myEmail = b.getString("my_email");

            if (emails == null) {
                //For now do nothing TODO
            } else {
                listEmails.setAdapter(new ArrayAdapter<String>(AddDirectChannelActivity.this,
                        R.layout.format_text_navigation, emails));

                listEmails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item text from ListView
                        try {
                            String selectedItem = (String) parent.getItemAtPosition(position);
                            String name = selectedItem + myEmail;
                            /*preference.saveActualChannel(new Channel(name));
                            changeActivity(AddDirectChannelActivity.this, ChatActivity.class);*/
                            loadingSpin.showDialog(AddDirectChannelActivity.this);
                            String currentWorkspace = preference.getActualWorkspace().getName();

                            List<String> users = new ArrayList<>();
                            users.add(myEmail);
                            users.add(selectedItem);

                            NewChannel newChannel = new NewChannel(currentWorkspace,
                                    name,
                                    WELCOME_MESSAGE,
                                    DESCRIPTION,
                                    users,
                                    "users");

                            createChannel(newChannel);
                        }catch (Exception e){
                            Log.d("ERROR", e.getMessage());
                        }
                    }
                });
            }
        } else {
            changeActivity(AddDirectChannelActivity.this, ChatActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        changeActivity(AddDirectChannelActivity.this, ChatActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();*/
                changeActivity(AddDirectChannelActivity.this, ChatActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createChannel(final NewChannel channel) {
        String workName = preference.getActualWorkspace().getName();
        channelRequester.createChannel(channel,workName,preference.getToken(), new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                AddDirectChannelActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                    }
                });

                try{
                    String msg = response.body().string();
                    final Channel channel1 = new Gson().fromJson(msg,Channel.class);

                    if(response.isSuccessful()){
                        AddDirectChannelActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                preference.saveActualChannel(channel1);
                                changeActivity(AddDirectChannelActivity.this, ChatActivity.class);
                            }
                        });
                    }
                    Log.d("CreateWork/loadData", msg);

                }catch (Exception e){
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CreateWork/loadData", e.getMessage());
                call.cancel();
                AddDirectChannelActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingSpin.hideDialog();
                    }
                });
            }
        });
    }
}
