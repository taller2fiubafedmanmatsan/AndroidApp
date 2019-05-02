package com.taller2.droidclient.servicies;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taller2.droidclient.utils.JsonConverter;
import com.taller2.droidclient.utils.SavedState;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "FIREBASE/MSG";
    private LocalBroadcastManager broadcaster;

    public MessagingService() {}

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Intent intent = new Intent("Messages");
        //intent.putExtra("msg", remoteMessage.getNotification().getBody());

        /*data: {
                    msg: message.text,
                    createdAt: message.dateTime.toString(),
                    workspace: workspace.name.toString(),
                    channel: channel.name.toString(),
                    sender_name: sender.name.toString(),
                    sender_email: sender.email.toString(),
                    sender_nickname: sender.nickname.toString()
        }*/
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        //intent.putExtra("msg", remoteMessage.getData().get("msg"));

        /*intent.putExtra("sender", new JsonConverter().objectToJsonString(remoteMessage.getData().get("sender")));

        intent.putExtra("sender", remoteMessage.getData().get("sender"));
        intent.putExtra("createdAt", remoteMessage.getData().get("createdAt"));*/
        broadcaster.sendBroadcast(intent);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
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
