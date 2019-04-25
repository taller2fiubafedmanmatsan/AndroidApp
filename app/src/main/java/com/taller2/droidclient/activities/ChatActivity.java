package com.taller2.droidclient.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.model.Workspace;

import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends BasicActivity
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerChannelsList;
    private ListView mDrawerMessagesList;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences preferences;
    private Button buttonCreateChannel;
    //private PopupWindow mPopupWindow;
    //private ConstraintLayout mConstraintLayout;
    //Temporary strings
    //Change to Channel or UserChat

    private Workspace[] workspaces;

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
        setContentView(R.layout.activity_chat);

        //Lista de mensajes

        //List<BaseMessage> messageList = new LinkedList<BaseMessage>();
        List<UserMessage> messageList = new LinkedList<UserMessage>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Workspace name");

        messageList.add(new UserMessage("Hola",
                new User("0", "Juan", "admin@gmail.com", "juansoy", true),
                4040));

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        mMessageAdapter.notifyDataSetChanged();

        //mConstraintLayout = findViewById(R.id.constraint_layout);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerChannelsList = findViewById(R.id.left_channel_drawer);
        mDrawerMessagesList = findViewById(R.id.left_messages_drawer);
        buttonCreateChannel = findViewById(R.id.icon_create_channel);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setDrawersListeners();
        setListeners();

        preferences = getSharedPreferences("login",MODE_PRIVATE);
        token = this.getUserToken();

        retrieveWorkspaces();
        retrieveChannels();
        retrieveChats();
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
                changeActivity(ChatActivity.this,ProfileActivity.class, token);
                return true;
            case R.id.action_logout:
                preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();
                changeActivity(ChatActivity.this,MainActivity.class);
                Toast.makeText(ChatActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void retrieveWorkspaces() {

    }

    private void retrieveChannels() {
        //Add request and get channels
        mDrawerChannelsList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.format_text_navigation, channel));
    }

    private void retrieveChats() {
        //Add request and get chats
        mDrawerMessagesList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.format_text_navigation, message));
    }

    private void setListeners(){
        buttonCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ChatActivity.this, ChannelCreationActivity.class, token);
            }
        });
    }

    private void setDrawersListeners() {
        mDrawerChannelsList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ChatActivity.this, channel[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mDrawerMessagesList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ChatActivity.this, message[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
