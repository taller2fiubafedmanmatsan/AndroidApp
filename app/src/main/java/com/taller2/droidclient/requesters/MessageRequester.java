package com.taller2.droidclient.requesters;

import android.util.Log;

import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceResponse;
import com.taller2.droidclient.utils.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageRequester {
    //private String basicUrl = "https://hypechat-t2.herokuapp.com";
    private String basicUrl = "https://app-server-t2.herokuapp.com";

    private String postUrl = basicUrl + "/api/messages";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void sendMessage(String msg, WorkspaceResponse workspace, Channel channel, String token, CallbackUserRequester callback) {
        try{
            Map<String, String> msgMap = new HashMap<String,String>();
            msgMap.put("creator", "admin@gmail.com");
            msgMap.put("text",msg);

            postRequest(postUrl + "/workspace/" + workspace.getName() + "/channel/" + channel.getName(),
                    new JsonConverter().mapToJsonString(msgMap)
                    , token
                    , callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void postRequest(String postUrl, String postBody,String token, final CallbackUserRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder().url(postUrl).post(body).header("x-auth-token",token).build();

        Log.d("REQUEST MESSAGE", postBody);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }
}
