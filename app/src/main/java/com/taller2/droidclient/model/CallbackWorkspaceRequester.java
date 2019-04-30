package com.taller2.droidclient.model;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public interface CallbackWorkspaceRequester {
    public void onResponse(Call call, Response response) throws IOException;

    public void onFailure(Call call, IOException e);
}
