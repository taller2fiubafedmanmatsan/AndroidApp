package com.taller2.droidclient.requesters;

import android.util.Log;

import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.NewChannel;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.utils.JsonConverter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChannelRequester {
    //private String basicUrl = "https://hypechat-t2.herokuapp.com";
    private String basicUrl = "https://app-server-t2.herokuapp.com";

    private String postUrl = basicUrl + "/api/channels/workspace";
    private String getUrl = basicUrl + "/api/channels/";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public void getChannel(String channelName, String workName, String token, CallbackRequester callback){
        try{
            String url = getUrl+channelName +"/workspace/"+ workName;
            getRequest(url,token,callback);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createChannel(NewChannel channel,String workName, String token, CallbackWorkspaceRequester callback){
        try {
            String url = postUrl +"/"+workName;
            postRequest(url, new JsonConverter().objectToJsonString(channel),token, callback);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void postRequest(String postUrl, String postBody,String token, final CallbackWorkspaceRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder().url(postUrl).post(body).header("x-auth-token",token).build();

        Log.d("REQUEST", postBody);

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

    private void putRequest(String putUrl, String putBody, String token, final CallbackUserRequester callback) throws  IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON,putBody);
        Request request = new Request.Builder().url(putUrl).put(body).header("x-auth-token",token).build();

        Log.d("REQUEST", request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);

            }
        });
    }

    private void getRequest(String url, String token,final CallbackRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).header("x-auth-token", token).get().build();

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
