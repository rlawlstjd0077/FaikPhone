package com.faikphone.client.network;

import android.util.Log;

import com.faikphone.client.Requester;
import com.faikphone.client.data.Response;
import com.faikphone.client.data.ResponseHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by dsm_025 on 2017-05-11.
 */

public abstract class HttpClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public OkHttpClient client;
    public Requester requester;
    public ResponseHandler resHandler;


    public abstract void doFakeRegister(String token, String code);
    public abstract void doRealRegister(String token, String phoneNum);
    public abstract void doSendMessage(JSONObject msg, String token) throws JSONException;
    public abstract void doResetCode(String token);
    public abstract void doResetAll(String token);
    public abstract void doResetConnection(String token);
    public abstract void doCheckConnection(String token);


    public Request getRequest(HttpUrl.Builder builder, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(builder.build().toString())
                .post(body)
                .build();
    }

    public void doRequest(Request request, boolean state) {
        requester = new Requester(client, request);
        try {
            Thread thread = new Thread(requester);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Response response = new Gson().fromJson(requester.getResponseMessage(), Response.class);
        resHandler.handlerResponse(response, state);
    }

    public abstract HttpUrl.Builder createBuilder(String type, String token);
}

