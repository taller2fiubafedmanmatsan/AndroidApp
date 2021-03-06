package com.taller2.droidclient.requesters;

import android.util.Log;

import com.taller2.droidclient.model.Admins;
import com.taller2.droidclient.model.CallbackRequester;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.CallbackWorkspaceRequester;
import com.taller2.droidclient.model.Users;
import com.taller2.droidclient.model.Workspace;
import com.taller2.droidclient.model.WorkspaceUpdate;
import com.taller2.droidclient.utils.JsonConverter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WorkspaceRequester {
    //private String basicUrl = "http://192.168.0.8:3000";
    //private String basicUrl = "https://hypechat-t2.herokuapp.com";
    private String basicUrl = "https://app-server-t2.herokuapp.com";

    private String postUrl = basicUrl + "/api/workspaces";


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void joinWorkspace(Workspace work, String token, CallbackRequester callback){
        try{
            String patchUrl = postUrl +"/"+ work.getName();
            patchRequest(patchUrl,token,callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void updateWorkspace(WorkspaceUpdate work,String workPastName, String token, CallbackRequester callback){
        try{
            String patchUrl = postUrl +"/"+ workPastName + "/fields";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(work),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addAdmin(String workName, Admins admins, String token, CallbackRequester callback){
        try{
            String patchUrl = postUrl +"/"+ workName + "/addAdmins";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(admins),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeAdmin(String workName, Admins admins, String token, CallbackRequester callback){
        try{
            String patchUrl = postUrl +"/"+ workName + "/removeAdmins";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(admins),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addUser(String workName, Users users, String token, CallbackRequester callback){
        try{
            String patchUrl = postUrl +"/"+ workName + "/addUsers";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(users),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeUser(String workName, Users users, String token, CallbackRequester callback){
        try{
            String patchUrl = postUrl +"/"+ workName + "/removeUsers";
            patchRequest(patchUrl,token,new JsonConverter().objectToJsonString(users),callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public void createWorkspace(Workspace workspace, String token,CallbackWorkspaceRequester callback){
        try {
            postRequest(postUrl, new JsonConverter().objectToJsonString(workspace),token, callback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getWorkspace(String name,String token, CallbackWorkspaceRequester callback) {
        try {
            String url = postUrl +"/"+ name;
            getRequest(url, token, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorkspace(String name,String token, CallbackRequester callback) {
        try {
            String url = postUrl +"/"+ name;
            deleteRequest(url, token, callback);
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

    private void getRequest(String url, String token, final CallbackWorkspaceRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).header("x-auth-token", token).get().build();

        Log.d("REQUEST", request.toString());

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

    private void patchRequest(String patchUrl, String token, final CallbackRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, "");

        Request request = new Request.Builder().url(patchUrl).header("x-auth-token", token).patch(body).build();

        Log.d("PATCH/WORKSPACE", patchUrl);

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

        Log.d("PATCH/WORKSPACE", patchUrl);

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
