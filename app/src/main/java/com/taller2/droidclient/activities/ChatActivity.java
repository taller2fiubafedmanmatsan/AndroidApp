package com.taller2.droidclient.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.ChannelListAdapter;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.adapters.WorkspaceListAdapter;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.MessagesResponse;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.MessageRequester;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

    private ArrayList<String> directMessage;

    private ArrayList<WorkspaceResponse> workspaces;
    private ArrayList<Channel> actualChannels;
    private List<UserMessage> messageList;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;

    private MessageRequester messageRequester;
    private ChannelRequester channelRequester;
    private boolean chatLoaded = false;

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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!chatLoaded)
                return;
        /*data: {
                    msg: message.text,
                    createdAt: message.dateTime.toString(),
                    workspace: workspace.name.toString(),
                    channel: channel.name.toString(),
                    sender_name: sender.name.toString(),
                    sender_email: sender.email.toString(),
                    sender_nickname: sender.nickname.toString()
        }*/

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(intent.getExtras().getString("createdAt"));
                messageList.add(
                        new UserMessage(intent.getExtras().getString("msgId"),
                                intent.getExtras().getString("msg"),
                                new User(
                                        intent.getExtras().getString("sender_id"),
                                        intent.getExtras().getString("sender_name"),
                                        intent.getExtras().getString("sender_email"),
                                        intent.getExtras().getString("sender_nickname"),
                                        true),
                                date
                        ));
            } catch (ParseException e) {
                Log.d("EXCEPTION/RECVMSG", e.getMessage());
                return;
            }
            /*messageList.add(new UserMessage("1", intent.getExtras().getString("msg"),
                    new User("0", "Juan", "admin@gmail.com", "soy_juan", true),
                    4040));*/

            /*User user_sender = new Gson().fromJson(intent.getExtras().getString("sender"), User.class);
            String msg = intent.getExtras().getString("msg");
            long createdAt = intent.getExtras().getLong("createdAt");

            messageList.add(new UserMessage(msg, user_sender, createdAt));*/

            mMessageAdapter.notifyDataSetChanged();
            mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
        }
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
        messageRequester = new MessageRequester();
        channelRequester = new ChannelRequester();
        directMessage = new ArrayList<>();

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

        workspaces = new ArrayList<WorkspaceResponse>();
        actualChannels = new ArrayList<Channel>();

        loadUserdata();
        retrieveWorkspaces();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Messages")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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
                                    if (workspaces.isEmpty())
                                        changeActivity(ChatActivity.this, NoWorkspaceActivity.class);

                                    preference.saveActualWorkspace(workspaces.get(0));
                                    //workspaces.add(actual_workspace);
                                }
                                WorkspaceListAdapter adapter = new WorkspaceListAdapter(ChatActivity.this, workspaces);
                                mDrawerWorkspaceList.setAdapter(adapter);
                                retrieveChannels(workspaces.get(workspaces.indexOf(actual_workspace)));
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
                        actualChannels = (ArrayList<Channel>) work.getChannels();

                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Channel actual_channel = preference.getActualChannel();
                                /*if (!actualChannels.contains(actual_channel) && !actual_channel.getName().isEmpty()) {
                                    actualChannels.add(actual_channel);
                                }*/

                                if (!actualChannels.contains(actual_channel)) {
                                    preference.saveActualChannel(actualChannels.get(0));
                                }

                                ChannelListAdapter adapter = new ChannelListAdapter(ChatActivity.this, actualChannels);
                                mDrawerChannelsList.setAdapter(adapter);
                                //retrieveChats(workspaces.get(workspaces.indexOf(preference.getActualWorkspace())));
                                loadMessagesActualChannel(preference.getActualChannel());
                                setListenersChannels();
                                for (User user:work.getUsers()) {
                                    directMessage.add(user.getName());
                                }

                                mDrawerMessagesList.setAdapter(new ArrayAdapter<String>(ChatActivity.this,
                                        R.layout.format_text_navigation, directMessage));
                            }
                        });
                    }
                    Log.d("LOAD/CHANNEL", msg);
                }catch (Exception e){
                    Log.d("LOAD/CHANNEL", e.getMessage());
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

    //Deprecadisimo
    private void retrieveChats(WorkspaceResponse actual_workspace) {
        String channelName = preference.getActualChannel().getName();
        String workName = actual_workspace.getName();
        String token = preference.getToken();
        channelRequester.getChannel(channelName, workName, token, new CallbackRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String msg = response.body().string();
                    //final Channel channel = new Gson().fromJson(msg, Channel.class);
                    if (response.isSuccessful()) {

                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDrawerMessagesList.setAdapter(new ArrayAdapter<String>(ChatActivity.this,
                                        R.layout.format_text_navigation, message));
                            }
                        });
                    }
                    Log.d("LOAD/messages", msg);
                }catch (Exception e){
                    Log.d("LOAD/messages", e.getMessage());
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("LOAD/Message", e.getMessage());
                call.cancel();
                finish();

            }
        });
    }

    private void setListeners(){
        buttonCreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(ChatActivity.this, ChannelCreationActivity.class);
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
                changeActivityNotFinish(ChatActivity.this, WorkspaceJoinActivity.class);
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

    // Esto hay que verlo
    private void setListenersMessage(){
        mDrawerMessagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                try {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    String name = selectedItem;
                    preference.saveActualChannel(new Channel(name));
                    changeActivity(ChatActivity.this, StartLoadingActivity.class);
                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
                }
            }
        });
    }


    private void setListenersChannels(){
        mDrawerChannelsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                try {
                    Channel selectedItem = (Channel) parent.getItemAtPosition(position);
                    String channelName = selectedItem.getName();
                    preference.saveActualChannel(new Channel(channelName));
                    changeActivity(ChatActivity.this, ChatActivity.class);
                }catch (Exception e){
                    Log.d("ERROR", e.getMessage());
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
                    preference.saveActualChannel(new Channel(""));
                    changeActivity(ChatActivity.this, ChatActivity.class);
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



        /*messageList.add(new UserMessage(msg,
                new User("0", "Juan", "admin@gmail.com", "soy_juan", true),
                4040));

        mMessageAdapter.notifyDataSetChanged();
        mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());*/

        Log.d("SENDING/MSG/WORKSPACE", preference.getActualWorkspace().getName());
        Log.d("SENDING/MSG/CHANNEL", preference.getActualChannel().getName());

        messageRequester.sendMessage(msg,
                preference.getActualWorkspace(),
                preference.getActualChannel(),
                preference.getToken(),
                new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful())
                    Log.d("SENDING/MSG", "Sucessful");

                Log.d("SENDING/MSG", response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("SENDING/MSG", "Failure");
            }
        });
    }

    private void loadMessagesActualChannel(Channel channel) {
        channelRequester.getChannel(channel.getName(),
                preference.getActualWorkspace().getName(),
                preference.getToken(),
                new CallbackRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String msg = response.body().string();
                        Log.d("LOADING/MSGS", msg);
                        if (response.isSuccessful()) {
                            /*final MessagesResponse messages = new Gson().fromJson(msg, MessagesResponse.class);
                            messageList = messages.getPages().get(0).getMessages();*/
                            chatLoaded = true;
                        }

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("LOADING/MSGS", e.getMessage());
                        call.cancel();
                    }
                });
    }

    private void loadUserdata() {

    }

    /*public User getUser() {

    }*/

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
