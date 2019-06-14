package com.taller2.droidclient.servicies;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taller2.droidclient.R;
import com.taller2.droidclient.activities.MainActivity;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.utils.JsonConverter;
import com.taller2.droidclient.utils.SavedState;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "FIREBASE/MSG";
    private LocalBroadcastManager broadcaster;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_IMAGE_RECEIVED = 3;
    private static final int VIEW_TYPE_MAP_RECEIVED = 4;
    private static final int VIEW_TYPE_FILE_RECEIVED = 5;
    private static final int VIEW_TYPE_SNIPPET_RECEIVED = 6;

    public MessagingService() {}

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }*/

        SavedState preference = new SavedState(getApplicationContext());
        if (!preference.isAppRunning())
            sendNotification(remoteMessage, preference);

        Intent intent = new Intent("Messages");

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        broadcaster.sendBroadcast(intent);
    }

    private void sendNotification(RemoteMessage remoteMessage, SavedState preference) {
        String channel_name = remoteMessage.getData().get("channel");
        String workspace_name = remoteMessage.getData().get("workspace");

        preference.saveActualChannel(new Channel(channel_name));
        preference.saveActualWorkspace(new WorkspaceResponse(workspace_name));

        Log.d(TAG, channel_name);

        String channel_type = remoteMessage.getData().get("channelType");
        String title = workspace_name;

        if (!channel_type.equals("users")) {
            title += " - " + channel_name;
        }

        String text = "";

        int msg_type = Integer.valueOf(remoteMessage.getData().get("msgType"));

        switch (msg_type) {
            case VIEW_TYPE_MESSAGE_RECEIVED: {
                text = remoteMessage.getData().get("sender_name") + ": " +remoteMessage.getData().get("msg");
                break;
            }
            case VIEW_TYPE_IMAGE_RECEIVED: {
                text = remoteMessage.getData().get("sender_name") + " sent an image";
                break;
            }
            case VIEW_TYPE_MAP_RECEIVED: {
                text = remoteMessage.getData().get("sender_name") + " sent his/her location";
                break;
            }
            case VIEW_TYPE_FILE_RECEIVED: {
                text = remoteMessage.getData().get("sender_name") + " sent a file";
                break;
            }
            case VIEW_TYPE_SNIPPET_RECEIVED: {
                text = remoteMessage.getData().get("sender_name") + " sent a snippet of code";
                break;
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(title)
                .setContentText(text/*remoteMessage.getNotification().getBody()*/)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        //super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        //SharedPreferences preference = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        //Log.d("FIREBASE/MSGTESTING", preference.getString("token", ""));

        SavedState preference = new SavedState(getApplicationContext());

        preference.saveActualFCM(token);
        //Log.d("FIREBASE/MSGTESTING",preference.getToken());
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
}
