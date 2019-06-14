package com.taller2.droidclient.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.ArraySet;
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

import com.bumptech.glide.Glide;
import com.github.tutorialsandroid.filepicker.controller.DialogSelectionListener;
import com.github.tutorialsandroid.filepicker.model.DialogConfigs;
import com.github.tutorialsandroid.filepicker.model.DialogProperties;
import com.github.tutorialsandroid.filepicker.view.FilePickerDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.ChannelListAdapter;
import com.taller2.droidclient.adapters.DirectListAdapter;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.adapters.WorkspaceListAdapter;
import com.taller2.droidclient.model.BaseMessage;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.ChannelResponse;
import com.taller2.droidclient.model.MessagesResponse;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.model.UserMail;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.requesters.MessageRequester;
import com.taller2.droidclient.requesters.ChannelRequester;
import com.taller2.droidclient.requesters.UserRequester;
import com.taller2.droidclient.requesters.WorkspaceRequester;
import com.taller2.droidclient.utils.DownloadDialog;
import com.taller2.droidclient.utils.SnippetDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.support.v4.app.FragmentActivity;

import org.apache.commons.lang3.RandomStringUtils;

import okhttp3.Call;
import okhttp3.Response;

public class ChatActivity extends BasicActivity {

    private final int REQUEST_LOC = 1;
    private final int REQUEST_STORAGE = 2;

    private final String ID_BOT = "BOT_ID";
    private final String NAME_BOT = "BOT_USER";
    private final String EMAIL_BOT = "BOT@BOT.COM";
    private final String URL_BOT_PHOTO = "https://static.giantbomb.com/uploads/square_medium/0/9266/1439593-team_gurren_logo_black_darkred.png";


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
    private Button buttonSendImage;
    private Button buttonSendLoc;
    private Button buttonSendFile;
    private Button buttonSendSnippet;
    private Button buttonAddUser;
    private LinearLayout layoutChannelAndMessages;
    private LinearLayout layoutWorkspace;
    private String currentUserEmail;

    //private PopupWindow mPopupWindow;
    //private ConstraintLayout mConstraintLayout;
    //Temporary strings
    //Change to Channel or UserChat

    private ArrayList<String> directMessage;

    private ArrayList<WorkspaceResponse> workspaces;
    private ArrayList<Channel> actualChannels;
    private ArrayList<Channel> actualDirectMessages;
    private List<UserMessage> messageList;
    private UserRequester userRequester;
    private WorkspaceRequester workspaceRequester;
    private ArrayList<String> workAdmins;

    private MessageRequester messageRequester;
    private ChannelRequester channelRequester;
    private boolean chatLoaded = false;
    private StorageReference mStorageRef;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private FilePickerDialog dialog;

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
                                date,
                                Integer.valueOf(intent.getExtras().getString("msgType")),
                                intent.getExtras().getString("sender_photoUrl")
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
        workAdmins = new ArrayList<>();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
        buttonSendImage = findViewById(R.id.button_send_image);
        buttonSendLoc = findViewById(R.id.button_send_loc);
        buttonSendFile = findViewById(R.id.button_send_file);
        buttonSendSnippet = findViewById(R.id.button_send_snippet);
        buttonBackWorkspaces = findViewById(R.id.icon_back_workspaces);
        buttonCreateWorkspace = findViewById(R.id.button_create_workspace);
        buttonJoinWorkspace = findViewById(R.id.button_join_workspace);
        layoutChannelAndMessages = findViewById(R.id.channel_nav);
        layoutWorkspace = findViewById(R.id.workspace_nav);
        buttonAddUser = findViewById(R.id.icon_add_user);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        DialogProperties properties = new DialogProperties();

        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        dialog = new FilePickerDialog(ChatActivity.this, properties);
        dialog.setTitle("Select a File");
        dialog.setPositiveBtnName("Select");
        dialog.setNegativeBtnName("Cancel");

        setDrawersListeners();
        setListeners();

        //preferences = getSharedPreferences("login",MODE_PRIVATE);

        workspaces = new ArrayList<WorkspaceResponse>();
        actualChannels = new ArrayList<Channel>();
        actualDirectMessages = new ArrayList<Channel>();

        loadUserdata();
        //retrieveWorkspaces();
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

            case R.id.action_workspace:
                changeActivityNotFinish(ChatActivity.this, WorkspaceActivity.class);
                return true;
            case R.id.action_channel:
                changeActivityNotFinish(ChatActivity.this, ChannelActivity.class);
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
                    Log.d("LOAD/CHANNEL", msg);
                    final WorkspaceResponse work = new Gson().fromJson(msg, WorkspaceResponse.class);
                    if (response.isSuccessful()) {
                        Log.d("LOAD/CHANNEL", msg);
                        final List<String> users_already_messaged = new ArrayList<>();
                        for (Channel channel : work.getChannels()) {

                            //Channel channel = new Channel(channelRes.getName(), channelRes.getChannelType());

                            if (channel.is_direct_channel()) {
                                List<User> users = channel.getUsers();
                                //List<String> users = channelRes.getUsers();

                                String firstEmail = users.get(0).getEmail();
                                String secondEmail = users.get(1).getEmail();
                                //String firstEmail = users.get(0);
                                //String secondEmail = users.get(1);
                                if (firstEmail.equals(currentUserEmail)) {
                                    channel.setFakeName(secondEmail);
                                    users_already_messaged.add(secondEmail);
                                    actualDirectMessages.add(channel);
                                } else if (secondEmail.equals(currentUserEmail)){
                                    channel.setFakeName(firstEmail);
                                    users_already_messaged.add(firstEmail);
                                    actualDirectMessages.add(channel);
                                }
                            } else {
                                actualChannels.add(channel);
                            }
                        }
                        //actualChannels = (ArrayList<Channel>) work.getChannels();

                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Channel actual_channel = preference.getActualChannel();
                                /*if (!actualChannels.contains(actual_channel) && !actual_channel.getName().isEmpty()) {
                                    actualChannels.add(actual_channel);
                                }*/

                                if ((!actualChannels.contains(actual_channel) && !actualChannels.isEmpty())
                                && !actualDirectMessages.contains(actual_channel)) {
                                    preference.saveActualChannel(actualChannels.get(0));
                                }

                                ChannelListAdapter adapter = new ChannelListAdapter(ChatActivity.this, actualChannels);
                                mDrawerChannelsList.setAdapter(adapter);

                                DirectListAdapter adapter_direct = new DirectListAdapter(ChatActivity.this, actualDirectMessages);
                                mDrawerMessagesList.setAdapter(adapter_direct);
                                //retrieveChats(workspaces.get(workspaces.indexOf(preference.getActualWorkspace())));
                                loadMessagesActualChannel(preference.getActualChannel());
                                setListenersChannels();
                                for (User user:work.getUsers()) {
                                    if (user.getEmail().equals(currentUserEmail) ||
                                            user_already_messaged_in(users_already_messaged, user))
                                        continue;
                                    directMessage.add(user.getEmail());
                                }
                                for (User user:work.getAdmins()) {
                                    workAdmins.add(user.getEmail());
                                }
                                /*mDrawerMessagesList.setAdapter(new ArrayAdapter<String>(ChatActivity.this,
                                        R.layout.format_text_navigation, directMessage));*/
                            }
                        });
                    }
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

    private boolean user_already_messaged_in(final List<String> users, User user) {
        for (String mail : users) {
            if (mail.equals(user.getEmail()))
                return true;
        }

        return false;
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

        buttonSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_gallery();
            }
        });

        buttonSendLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    ActivityCompat.requestPermissions(ChatActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOC);
                else
                    Log.d("LOCATION", "Require SDK Level >= 23");
                //changeActivityNotFinish(ChatActivity.this, MapsActivity.class);
            }
        });

        buttonSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                sendFileMessage(new File(files[0]));
            }
        });

        buttonSendSnippet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnippetDialog snippet_dialog = new SnippetDialog();

                snippet_dialog.showDialog(ChatActivity.this);
            }
        });

        /*buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workAdmins.contains(currentUserEmail)){
                    changeActivityNotFinish(ChatActivity.this,AddUserWorkspaceActivity.class);
                }else{
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChatActivity.this, "No tiene permisos para realizar esta accion.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });*/

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddDirectChannelActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("emails", directMessage);
                b.putString("my_email", currentUserEmail);
                intent.putExtras(b); //Put your id to your next Intent
                v.getContext().startActivity(intent);
                finish();
                //changeActivity(ChatActivity.this,AddDirectChannelActivity.class);
            }
        });
    }

    public void downloadFile(final String filename, final String url) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission
                    (ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("PERMISSION/WRITE", "Granted");
                        downloadFromUrl(filename, url);
            } else {
                Log.d("PERMISSION/WRITE", "Asking");
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE); }
        } else {
            downloadFromUrl(filename, url);
        }
    }

    private void downloadFromUrl(String filename, String url) {
        Log.d("PERMISSION/WRITE", "Downloading");

        DownloadDialog dialog = new DownloadDialog(filename, url);

        dialog.showDialog(this);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        String locationProvider = LocationManager.NETWORK_PROVIDER;

                        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                        Log.d("LOCATION", String.valueOf(lastKnownLocation.getLatitude()));

                        sendLocationMessage(lastKnownLocation);
                    } catch (SecurityException e) {
                        Log.d("LOCATION", e.getMessage());
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("LOCATION", "Service not granted");
                }
                return;
            }
            case REQUEST_STORAGE: {
                /*if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        String locationProvider = LocationManager.NETWORK_PROVIDER;

                        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                        Log.d("LOCATION", String.valueOf(lastKnownLocation.getLatitude()));

                        sendLocationMessage(lastKnownLocation);
                    } catch (SecurityException e) {
                        Log.d("LOCATION", e.getMessage());
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("LOCATION", "Service not granted");
                }*/
                return;
            }
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(ChatActivity.this,"Permission is required for sending files",Toast.LENGTH_SHORT).show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*private boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }*/

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

        mDrawerMessagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        messageRequester.sendMessageTxt(msg,
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

    private void sendImageMessage(Bitmap bitmap) {
        //loadingSpin.showDialog(ConfigRegisterActivity.this);
        //TO DO: Replace with random string
        Log.d("SENDING/MSG/WORKSPACE", preference.getActualWorkspace().getName());
        Log.d("SENDING/MSG/CHANNEL", preference.getActualChannel().getName());

        final String random_str = RandomStringUtils.randomAlphanumeric(25).toUpperCase();

        final String image_name = random_str + ".jpg";
        StorageReference mountainsRef = mStorageRef.child(image_name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ChatActivity.this, "Image failed to load: Try again", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!urlTask.isSuccessful());

                final Uri downloadUrl = urlTask.getResult();
                //final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                messageRequester.sendMessageImg(
                        downloadUrl,
                        preference.getActualWorkspace(),
                        preference.getActualChannel(),
                        preference.getToken(),
                        new CallbackUserRequester() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful())
                                    Log.d("SENDING/IMG", "Sucessful");

                                Log.d("SENDING/IMG", response.body().string());
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("SENDING/IMG", "Failure");
                            }
                        });

            }
        });
    }

    private void sendLocationMessage(Location location) {
        Log.d("SENDING/MSG/WORKSPACE", preference.getActualWorkspace().getName());
        Log.d("SENDING/MSG/CHANNEL", preference.getActualChannel().getName());

        messageRequester.sendMessageLocation(location.getLatitude(),
                location.getLongitude(),
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

    private void sendFileMessage(final File file) {
        Log.d("SENDING/MSG/WORKSPACE", preference.getActualWorkspace().getName());
        Log.d("SENDING/MSG/CHANNEL", preference.getActualChannel().getName());

        final String random_str = RandomStringUtils.randomAlphanumeric(25).toUpperCase();

        StorageReference mountainsRef = mStorageRef.child(random_str + "/" + file.getName());

        Uri file_uri = Uri.fromFile(file);

        //StorageReference riversRef = mStorageRef.child("files/"+file_uri.getLastPathSegment());
        UploadTask uploadTask = mountainsRef.putFile(file_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ChatActivity.this, "File failed to load: Try again", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!urlTask.isSuccessful());

                final Uri downloadUrl = urlTask.getResult();

                messageRequester.sendMessageFile(
                        file.getName(),
                        downloadUrl,
                        preference.getActualWorkspace(),
                        preference.getActualChannel(),
                        preference.getToken(),
                        new CallbackUserRequester() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful())
                                    Log.d("SENDING/FILE", "Sucessful");

                                Log.d("SENDING/FILE", response.body().string());
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("SENDING/FILE", "Failure");
                            }
                        });
            }
        });
    }

    public void sendSnippetMessage(String snippet) {
        Log.d("SENDING/MSG/WORKSPACE", preference.getActualWorkspace().getName());
        Log.d("SENDING/MSG/CHANNEL", preference.getActualChannel().getName());

        messageRequester.sendMessageSnippet(snippet,
                preference.getActualWorkspace(),
                preference.getActualChannel(),
                preference.getToken(),
                new CallbackUserRequester() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful())
                            Log.d("SENDING/SNIPPET", "Sucessful");

                        Log.d("SENDING/SNIPPET", response.body().string());
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("SENDING/SNIPPET", "Failure");
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
                            final MessagesResponse messages = new Gson().fromJson(msg, MessagesResponse.class);
                            List<BaseMessage> msgs = messages.getPages().get(0).getMessages();
                            List<UserMail> users = messages.getUsers();
                            HashMap<String, UserMail> users_map = new HashMap<>();

                            for (int i = 0; i < users.size(); i++) {
                                users_map.put(users.get(i).get_id(), users.get(i));
                            }

                            for (int i = 0; i < msgs.size(); i++) {
                                User infektedUser;
                                String infektedUrl;
                                String keyUser = msgs.get(i).getCreator();
                                if (users_map.containsKey(keyUser)){
                                    infektedUser =new User(users_map.get(keyUser).get_id(), users_map.get(keyUser).getName(),users_map.get(keyUser).getEmail(), users_map.get(keyUser).getNickname(),true);
                                    infektedUrl = users_map.get(keyUser).getPhotoUrl();
                                }else {
                                    infektedUser = new User(ID_BOT,NAME_BOT,EMAIL_BOT,NAME_BOT,true);
                                    infektedUrl = URL_BOT_PHOTO;
                                }
                                messageList.add(new UserMessage(
                                        msgs.get(i).get_id(),
                                        msgs.get(i).getText(),
                                        infektedUser,
                                        msgs.get(i).getDateTime(),
                                        Integer.valueOf(msgs.get(i).getType()),
                                        infektedUrl));
                            }

                            //messageList = msgs_save;

                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMessageAdapter.notifyDataSetChanged();
                                    mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
                                }
                            });

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
        userRequester.getUser(preference.getToken(), new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                final User user = new Gson().fromJson(msg, User.class);
                if (response.isSuccessful()) {
                    currentUserEmail = user.getEmail();
                    retrieveWorkspaces();
                }else{
                    changeActivity(ChatActivity.this,LoginActivity.class);
                }
                Log.d("CHAT/USER", msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CHAT/USER", e.getMessage());
                call.cancel();

            }
        });

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
                        Channel channel = actualDirectMessages.get(position);
                        Toast.makeText(ChatActivity.this, channel.getName(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ChatActivity.this, message[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mDrawerMessagesList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // userEmail = directMessage.get(position);
                Channel channel = actualDirectMessages.get(position);
                //String userEmail = channel.getName();
                String userEmail = channel.getFakeName();
                if(userEmail.equals(currentUserEmail)){
                    changeActivityNotFinish(ChatActivity.this,ProfileActivity.class);
                }else{
                    changeActivityNotFinish(ChatActivity.this,UserActivity.class,userEmail,currentUserEmail);
                }

                return true;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    CropImage.activity(data.getData())
                            .start(ChatActivity.this);
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(ChatActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ChatActivity.this.getContentResolver(), resultUri);
                    sendImageMessage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
