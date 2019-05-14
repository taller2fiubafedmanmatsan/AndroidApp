package com.taller2.droidclient.requesters;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.EmailRecoverUser;
import com.taller2.droidclient.model.LoginUser;
import com.taller2.droidclient.model.PasswordUser;
import com.taller2.droidclient.model.RegisterUser;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.utils.JsonConverter;

import org.json.JSONObject;

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

public class UserRequester {
    private String basicUrl = "https://hypechat-t2.herokuapp.com";
    //private String basicUrl = "https://app-server-t2.herokuapp.com";

    private String postUrl = basicUrl + "/api/users";
    private String recoverPassUrl = basicUrl + "/api/users/restorepassword";
    private String authUrl = basicUrl + "/api/auth/signin";
    private String facebookUrl = basicUrl + "/api/auth/facebook";
    private String fcmUrl = postUrl + "/fbtoken/";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public void patchTokenFCM(String token, String tokenFCM, CallbackUserRequester callback) {
        try {
            patchRequest(fcmUrl, token, tokenFCM, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(RegisterUser user, CallbackUserRequester callback){
        try {
            postRequest(postUrl, new JsonConverter().objectToJsonString(user), callback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recoverPassword(EmailRecoverUser email, CallbackUserRequester callback){
        try{
            postRequest(recoverPassUrl, new JsonConverter().objectToJsonString(email), callback);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loginUser(LoginUser user, CallbackUserRequester callback) {
        try {
            postRequest(authUrl, new JsonConverter().objectToJsonString(user), callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void facebookLogin(String token, CallbackUserRequester callback) {
        try {
            /*Map<String, String> mapToken = new HashMap<String,String>();
            mapToken.put("access_token",token);*/

            postRequestFacebook(facebookUrl, token/*new JsonConverter().objectToJsonString(mapToken)*/, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePasswordUser(PasswordUser pass, String token, CallbackUserRequester callback) {
        try{
            putRequest(postUrl + "/me",new JsonConverter().objectToJsonString(pass), token,callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void changeNicknameUser(String nickname, String token, CallbackUserRequester callback){
        try{
            Map<String, String> nicknameMap = new HashMap<String,String>();
            nicknameMap.put("nickname",nickname);

            putRequest(postUrl+ "/me",new JsonConverter().mapToJsonString(nicknameMap),token,callback);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void changeProfilePic(Uri url, String token, CallbackUserRequester callback){
        try{
            Map<String, String> urlMap = new HashMap<String,String>();
            urlMap.put("photoUrl",url.toString());

            putRequest(postUrl+ "/me",new JsonConverter().mapToJsonString(urlMap),token,callback);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void getUser(String id, CallbackUserRequester callback) {
        try {
            getRequest(postUrl + "/me", id, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getOtherUser(String userEmail,String token, CallbackUserRequester callback) {
        try {
            getRequest(postUrl +"/"+userEmail, token, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRequest(String url, String id, final CallbackUserRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).header("x-auth-token", id).get().build();

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

    private void postRequest(String postUrl, String postBody, final CallbackUserRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder().url(postUrl).post(body).build();

        Log.d("DEBUG", postBody);

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

    private void patchRequest(String patchUrl, String id, String parameter, final CallbackUserRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, "");

        Request request = new Request.Builder().url(patchUrl + parameter).header("x-auth-token", id).patch(body).build();

        Log.d("PATCH/USER", parameter);

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

    private void postRequestFacebook(String postUrl, String postHeader, final CallbackUserRequester callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        //RequestBody body = RequestBody.create(JSON, "");
        RequestBody body = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder().url(postUrl)
                .method("POST", body)
                .header("access_token", postHeader)
                .build();

        Log.d("DEBUG", postHeader);

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


}
