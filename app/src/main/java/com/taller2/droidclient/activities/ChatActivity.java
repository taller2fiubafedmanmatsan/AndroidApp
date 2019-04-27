package com.taller2.droidclient.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
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
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.ChannelListAdapter;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.adapters.WorkspaceListAdapter;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.model.Workspace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.support.v4.app.FragmentActivity;


public class ChatActivity extends BasicActivity
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerChannelsList;
    private ListView mDrawerMessagesList;
    private ListView mDrawerWorkspaceList;
    private ActionBarDrawerToggle mDrawerToggle;
    //private SharedPreferences preferences;
    private EditText textMessage;
    private Button buttonCreateChannel;
    private Button buttonWorkspaces;
    private Button buttonBackWorkspaces;
    private Button buttonCreateWorkspace;
    private Button buttonJoinWorkspace;
    private Button buttonSendText;
    private LinearLayout layoutChannelAndMessages;
    private LinearLayout layoutWorkspace;

    //private PopupWindow mPopupWindow;
    //private ConstraintLayout mConstraintLayout;
    //Temporary strings
    //Change to Channel or UserChat

    private ArrayList<Workspace> workspaces;
    private ArrayList<Channel> actualChannels;
    private List<UserMessage> messageList;

    private String[] channel = {"# General",
            "# Random",
            "# Imagenes",
            "# Memes",
            "# Parciales resueltos BDD",
            "# Falopa",
            "# Necesito mas casos",
            "# Filling list?",
            "# Is it filled!?"
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(preference.getActualWorkspace().getName());

        messageList = new LinkedList<UserMessage>();

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        //mMessageAdapter.notifyDataSetChanged();

        //mConstraintLayout = findViewById(R.id.constraint_layout);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerChannelsList = findViewById(R.id.left_channel_drawer);
        mDrawerMessagesList = findViewById(R.id.left_messages_drawer);
        mDrawerWorkspaceList = findViewById(R.id.left_workspaces_drawer);
        textMessage = findViewById(R.id.edittext_chatbox);
        buttonCreateChannel = findViewById(R.id.icon_create_channel);
        buttonWorkspaces = findViewById(R.id.icon_show_workspaces);
        buttonSendText = findViewById(R.id.button_chatbox_send);
        buttonBackWorkspaces = findViewById(R.id.icon_back_workspaces);
        buttonCreateWorkspace = findViewById(R.id.button_create_workspace);
        buttonJoinWorkspace = findViewById(R.id.button_join_workspace);
        layoutChannelAndMessages = findViewById(R.id.channel_nav);
        layoutWorkspace = findViewById(R.id.workspace_nav);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setDrawersListeners();
        setListeners();

        //preferences = getSharedPreferences("login",MODE_PRIVATE);
        token = preference.getToken();//this.getUserToken();

        workspaces = new ArrayList<Workspace>();
        actualChannels = new ArrayList<Channel>();

        retrieveWorkspaces();
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
                //changeActivity(ChatActivity.this,ProfileActivity.class, token);
                changeActivityNotFinish(ChatActivity.this, ProfileActivity.class);
                return true;
            case R.id.action_logout:
                /*preferences.edit().putBoolean("logged",false).apply();
                preferences.edit().putString("token", "").apply();*/
                preference.logout();
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
        //Add request and get workspaces

        //**DO REQUEST**
        //Retrieve all workspaces from this user
        //If user doesn't have any workspace, well, that's weird, change activity to
        //NoWorkspaceActivity
        //If it has, store them in workspaces
        //After it finishes (and succeeds) call retrieveChannels/retrieveChats with actual workspace
        //Again, if it doesn't have an actual workspace, set one please (?

        Workspace workspace_test1 = new Workspace("1", "Taller de programacion II");
        Workspace workspace_test2 = new Workspace("2", "Bases de datos");

        workspaces.add(workspace_test1);
        workspaces.add(workspace_test2);

        Workspace actual_workspace = preference.getActualWorkspace();


        if (!workspaces.contains(actual_workspace)) {
            //Well, set one (?
            //For now it finishes for testing purpose
            workspaces.add(actual_workspace);
            //finish();
        }

        WorkspaceListAdapter adapter = new WorkspaceListAdapter(this, workspaces);

        mDrawerWorkspaceList.setAdapter(adapter);

        retrieveChannels(workspaces.get(workspaces.indexOf(actual_workspace)));
        retrieveChats(workspaces.get(workspaces.indexOf(actual_workspace)));
    }

    private void retrieveChannels(Workspace actual_workspace) {
        //**DO REQUEST**
        //Get all the channels that I AM INTO in the actual workspace
        //After that, add channels to the actual workspace using actual_workspace.addChannel(channel)->Deprecated temporarily
        //then set de Adapter as below

        actualChannels.add(new Channel("1", "# General"));
        actualChannels.add(new Channel("2", "# Alumnos"));
        /*actual_workspace.addChannel(new Channel("1", "# General"));
        actual_workspace.addChannel(new Channel("2", "# Alumnos"));*/

        ChannelListAdapter adapter = new ChannelListAdapter(this, actualChannels);

        mDrawerChannelsList.setAdapter(adapter);
    }

    private void retrieveChats(Workspace actual_workspace) {
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

        buttonWorkspaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.setDuration(300);
                transition.addTarget(layoutWorkspace);

                TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content), transition);
                layoutWorkspace.setVisibility(View.VISIBLE);
                changeLayoutChannelAndMessageState(false);
            }
        });

        buttonBackWorkspaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.setDuration(500);
                transition.addTarget(layoutWorkspace);

                TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content), transition);
                layoutWorkspace.setVisibility(View.GONE);
                layoutChannelAndMessages.setVisibility(View.VISIBLE);
                changeLayoutChannelAndMessageState(true);
            }
        });

        buttonCreateWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivityNotFinish(ChatActivity.this, WorkspaceCreationActivity.class);
            }
        });

        buttonJoinWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textMessage.getText().toString();

                if (!msg.isEmpty()) {
                    textMessage.setText("");
                    sendTextMessage(msg);
                }
            }
        });
    }

    private void sendTextMessage(String msg) {
        //**DO REQUEST**
        //Send message to the server and that's it
        //Comment the next line after it's implemented
        //Im hoping  thatthe server sends our user data or we need to do a get to retrieve
        //that info (Contact Fede if we need to do a get)

        messageList.add(new UserMessage(msg,
                new User("0", "Juan", "admin@gmail.com", "soy_juan", true),
                4040));

        mMessageAdapter.notifyDataSetChanged();
        mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
    }

    private void changeLayoutChannelAndMessageState(boolean enable) {
        buttonCreateChannel.setEnabled(enable);
        mDrawerChannelsList.setEnabled(enable);
        mDrawerMessagesList.setEnabled(enable);
        buttonWorkspaces.setEnabled(enable);
    }

    private void setDrawersListeners() {
        mDrawerChannelsList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Channel channel = actualChannels.get(position);
                        //Channel channel = workspaces.get(workspaces.indexOf(preference.getActualWorkspace())).getChannels().get(position);
                        Toast.makeText(ChatActivity.this, channel.getName(), Toast.LENGTH_SHORT).show();
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
}
