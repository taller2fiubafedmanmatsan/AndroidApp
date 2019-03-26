package com.taller2.droidclient.utils;

import com.google.gson.Gson;

public class JsonConverter {

    public String objectToJsonString(Object object){

        return new Gson().toJson(object);
    }
}
