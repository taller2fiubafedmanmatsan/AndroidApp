package com.taller2.droidclient.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.model.BaseMessage;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.UserMessage;

import java.util.LinkedList;
import java.util.List;

public class MessageListActivity extends BasicActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerChannelsList;
    private ListView mDrawerMessagesList;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences preferences;
    private Button buttonCreateChannel;
    private PopupWindow mPopupWindow;
    private ConstraintLayout mConstraintLayout;
    //Temporary strings
    //Change to Channel or UserChat
    private String[] channels;
    private String[] direct_messages;

    private String[] channel = {"General",
            "Random",
            "Imagenes",
            "Memes",
            "Parciales resueltos BDD",
            "Falopa",
            "Necesito mas casos",
            "Filling list?",
            "Is it filled!?"
    };

    private String[] message = {"Juan",
            "Ignacio",
            "Diego",
            "Otro juan",
            "El de resueltos",
            "Dios",
            "Fede",
            "Santi",
            "Otro Santi pero Pinto"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        //Lista de mensajes

        //List<BaseMessage> messageList = new LinkedList<BaseMessage>();
        List<UserMessage> messageList = new LinkedList<UserMessage>();

        messageList.add(new UserMessage("Hola",
                        new User("0", "Juan", "admin@gmail.com", "juansoy", true),
                        4040));

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        mConstraintLayout = findViewById(R.id.constraint_layout);

        mDrawerLayout = findViewById(R.id.profile_layout);
        mDrawerChannelsList = findViewById(R.id.left_channel_drawer);
        mDrawerMessagesList = findViewById(R.id.left_messages_drawer);
        buttonCreateChannel = findViewById(R.id.icon_create_channel);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerChannelsList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.format_text_navigation, channel));

        mDrawerMessagesList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.format_text_navigation, message));

        setDrawersListeners();
        setListeners();

        preferences = getSharedPreferences("login",MODE_PRIVATE);
        token = this.getUserToken();
    }

    private void setListeners(){
        buttonCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.format_create_channel,null);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                mPopupWindow.setElevation(5.0f);
                mPopupWindow.setFocusable(true);

                Button closeButton = customView.findViewById(R.id.ib_close);
                //EditText insertText = customView.findViewById(R.id.edit_channel_name);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER,0,0);
            }
        });
    }

    private void setDrawersListeners() {
        mDrawerChannelsList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MessageListActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MessageListActivity.this, channel[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mDrawerMessagesList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MessageListActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MessageListActivity.this, message[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.action_profile:
                changeActivity(MessageListActivity.this,ProfileActivity.class, token);
                return true;
            case R.id.action_logout:
                preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();
                changeActivity(MessageListActivity.this,MainActivity.class);
                Toast.makeText(MessageListActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_message_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
