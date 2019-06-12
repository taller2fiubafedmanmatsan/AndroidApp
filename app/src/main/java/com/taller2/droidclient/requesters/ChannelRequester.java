package com.taller2.droidclient.requesters;

import android.util.Log;

import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Channel;
import com.taller2.droidclient.model.JoinChannel;
import com.taller2.droidclient.model.NewChannel;
import com.taller2.droidclient.model.Users;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.utils.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChannelRequester {
    private String basicUrl = "http://192.168.0.8:3000";
    //private String basicUrl = "https://hypechat-t2.herokuapp.com";
    //private String basicUrl = "https://app-server-t2.herokuapp.com";

    private String postUrl = basicUrl + "/api/channels/workspace";
    private String getUrl = basicUrl + "/api/channels/";
    private String patchGeneral = basicUrl + "/api/channels/General/workspace/";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public void getChannel(String channelName, String workName, String token, CallbackRequester callback){
        try{
            String url = getUrl+channelName +"/workspace/"+ workName;
            getRequest(url,token,callback);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteChannel(String channelName, String workName, String token, CallbackRequester callback){
        try{
            String url = getUrl+channelName +"/workspace/"+ workName;
            deleteRequest(url,token,callback);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String channelName, String workName, Users users, String token, CallbackRequester callback){
        try{
            String patchUrl = getUrl +channelName +"/workspace/"+ workName + "/addUsers";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(users),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeUser(String channelName, String workName, Users users, String token, CallbackRequester callback){
        try{
            String patchUrl = getUrl +channelName +"/workspace/"+ workName + "/users";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(users),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void changeChannel(String channelName, String workName, Channel channel, String token, CallbackRequester callback){
        try{
            String url = getUrl+channelName +"/workspace/"+ workName;
            patchRequest(url,token,new JsonConverter().objectToJsonString(channel),callback);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void joinChannel(String workName, List<String> users, String token, CallbackRequester callback){
        try{
            String url = patchGeneral+ workName +"/addUsers";
            patchRequest(url,token,new JsonConverter().objectToJsonString(new JoinChannel(users)),callback);

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

    private void patchRequest(String patchUrl, String token,String patchBody, final CallbackRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, patchBody);

        Request request = new Request.Builder().url(patchUrl).header("x-auth-token", token).patch(body).build();

        Log.d("PATCH/CHANNEL", patchUrl);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
                /*call.cancel();*/
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            } /*throws IOException {
                Log.d("LOG/Register",response.body().string());
            }*/
        });
    }

    private void deleteRequest(String url, String token,final CallbackRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).header("x-auth-token", token).delete().build();

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
