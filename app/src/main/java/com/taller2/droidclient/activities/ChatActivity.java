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

import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.ChannelListAdapter;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.adapters.WorkspaceListAdapter;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.support.v4.app.FragmentActivity;

import okhttp3.Call;
import okhttp3.Response;


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

    private ArrayList<WorkspaceResponse> workspaces;
    private ArrayList<String> actualChannels;
    private List<UserMessage> messageList;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;

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
        userRequester = new UserRequester();
        workspaceRequester = new WorkspaceRequester();

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

        workspaces = new ArrayList<WorkspaceResponse>();
        actualChannels = new ArrayList<String>();

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
        //**DO REQUEST**
        //Retrieve all workspaces from this user
        //If user doesn't have any workspace, well, that's weird, change activity to
        //NoWorkspaceActivity
        //If it has, store them in workspaces
        //After it finishes (and succeeds) call retrieveChannels/retrieveChats with actual workspace
        //Again, if it doesn't have an actual workspace, set one please (?
        userRequester.getUser(preference.getToken(), new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final User userdata = new Gson().fromJson(msg, User.class);
                    if (response.isSuccessful()) {
                        workspaces = (ArrayList<WorkspaceResponse>) userdata.getWorkspaces();


                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WorkspaceResponse actual_workspace = preference.getActualWorkspace();
                                if (!workspaces.contains(actual_workspace)) {
                                    workspaces.add(actual_workspace);
                                }
                                WorkspaceListAdapter adapter = new WorkspaceListAdapter(ChatActivity.this, workspaces);
                                mDrawerWorkspaceList.setAdapter(adapter);
                                retrieveChannels(workspaces.get(workspaces.indexOf(actual_workspace)));
                                retrieveChats(workspaces.get(workspaces.indexOf(actual_workspace)));
                                setListenersList();
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
                finish();
            }
        });

    }

    private void retrieveChannels(WorkspaceResponse actual_workspace) {
        workspaceRequester.getWorkspace(actual_workspace.getName(), preference.getToken(), new CallbackWorkspaceRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    final WorkspaceResponse work = new Gson().fromJson(msg, WorkspaceResponse.class);
                    if (response.isSuccessful()) {
                        actualChannels = (ArrayList<String>) work.getChannels();

                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Channel actual_channel = preference.getActualChannel();
                                if (!actualChannels.contains(actual_channel.getName())) {
                                    actualChannels.add(actual_channel.getName());
                                }
                                ArrayList<Channel> channels = new ArrayList<>();
                                for (String channel:actualChannels) {
                                    channels.add(new Channel(channel));
                                }
                                ChannelListAdapter adapter = new ChannelListAdapter(ChatActivity.this, channels);
                                mDrawerChannelsList.setAdapter(adapter);
                            }
                        });
                    }
                    Log.d("LOAD/CHANNEL", msg);
                }catch (Exception e){
                    finish();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("LOAD/CHANNEL", e.getMessage());
                call.cancel();
                finish();
            }
        });
    }

    private void retrieveChats(WorkspaceResponse actual_workspace) {
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


    private void setListenersList(){
        mDrawerWorkspaceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                try {
                    WorkspaceResponse selectedItem = (WorkspaceResponse) parent.getItemAtPosition(position);
                    String workName = selectedItem.getName();
                    preference.saveActualWorkspace(new WorkspaceResponse(workName));
                    changeActivity(ChatActivity.this, StartLoadingActivity.class);
                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
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
                        String channelName = actualChannels.get(position);
                        Channel channel = new Channel(channelName);
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

    private void loadWorkspaces() {

    }
}
