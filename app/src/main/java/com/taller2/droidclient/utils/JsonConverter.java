package com.taller2.droidclient.utils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

public class JsonConverter {

    public String objectToJsonString(Object object){

        return new Gson().toJson(object);
    }

    public String mapToJsonString(Map map){
        return new JSONObject(map).toString();
    }
}
