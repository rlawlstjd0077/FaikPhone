package com.faikphone.client.network;


import android.content.Context;

import com.faikphone.client.Requester;
import com.faikphone.client.data.Logger;
import com.faikphone.client.data.Response;
import com.faikphone.client.data.ResponseHandler;
import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by dsm_025 on 2017-04-13.
 */

public class RealHttpClient extends HttpClient{
    private String url = "http://192.168.137.181:8999/real.do";

    public RealHttpClient(Context context) {
        client = new OkHttpClient();
        resHandler = new ResponseHandler(context);
    }

    @Override
    public void doRegister(String token, String code) {
    }

    @Override
    public void doRegister(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("register", token);
        final Request request = getRequest(urlBuilder);
        doRequest(request, false);
    }

    @Override
    public String doSendMessage(String msg, String token) {
        HttpUrl.Builder urlBuilder = createBuilder("send_message", token);
        urlBuilder.addQueryParameter("message", msg);
        final Request request = getRequest(urlBuilder);
        doRequest(request, false);
        return null;
    }

    @Override
    public String doResetConnection(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_conn", token);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    @Override
    public void doResetCode(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_code", token);
        final Request request = getRequest(urlBuilder);
        doRequest(request, false);
    }

    @Override
    public String doResetAll(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_all", token);
        final Request request = getRequest(urlBuilder);
        return null;
    }


    @Override
    public HttpUrl.Builder createBuilder(String type, String token) {
        return HttpUrl.parse(url).newBuilder().addQueryParameter("type", type).addQueryParameter("token", token);
    }
}
