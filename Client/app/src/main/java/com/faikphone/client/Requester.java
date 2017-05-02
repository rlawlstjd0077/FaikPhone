package com.faikphone.client;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

/**
 * Created by dsm_025 on 2017-04-21.
 */

public class Requester implements Runnable {
    private Response response;
    private OkHttpClient client;
    private Request request;
    private String responseMessage;

    public Requester(OkHttpClient client, Request request) {
        this.client = client;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            response = client.newCall(request).execute();
            responseMessage = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
