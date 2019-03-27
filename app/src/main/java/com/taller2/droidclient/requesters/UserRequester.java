package com.taller2.droidclient.requesters;

import android.util.Log;

import com.google.gson.Gson;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.RegisterUser;
import com.taller2.droidclient.model.User;
import com.taller2.droidclient.utils.JsonConverter;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserRequester {

    private String postUrl = "https://app-server-t2.herokuapp.com/api/users";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public void registerUser(RegisterUser user, CallbackUserRequester callback){
        try {
            postRequest(postUrl, new JsonConverter().objectToJsonString(user), callback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postRequest(String postUrl, String postBody, final CallbackUserRequester callback) throws IOException{

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder().url(postUrl).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
                /*call.cancel();*/
            }
            @Override
            public void onResponse(Call call, Response response) {
                callback.onSuccess(call, response);
            } /*throws IOException {
                Log.d("LOG/Register",response.body().string());
            }*/
        });
    }


}
