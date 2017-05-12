package com.faikphone.client.network;

import android.content.Context;

import com.faikphone.client.Requester;
import com.faikphone.client.data.Response;
import com.faikphone.client.data.ResponseHandler;
import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dsm_025 on 2017-05-11.
 */

public class FakeHttpClient extends HttpClient{
    private String url = "http://192.168.137.181:8999/fake.do";

    public FakeHttpClient(Context context){
        client = new OkHttpClient();
        resHandler = new ResponseHandler(context);
    }

    @Override
    public void doRegister(String token, String code){
        HttpUrl.Builder urlBuilder = createBuilder("register", token);
        urlBuilder.addQueryParameter("code", code);
        final Request request = getRequest(urlBuilder);
        doRequest(request, true);
    }

    @Override
    public void doRegister(String token) {
    }

    @Override
    public String doSendMessage(String msg, String token) {
        HttpUrl.Builder urlBuilder = createBuilder("send_message", token);
        urlBuilder.addQueryParameter("message", msg);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    @Override
    public void doResetCode(String token) {
    }

    @Override
    public String doResetAll(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_all", token);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    @Override
    public String doResetConnection(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_conn", token);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    @Override
    public HttpUrl.Builder createBuilder(String type, String token) {
        return HttpUrl.parse(url).newBuilder().addQueryParameter("type", type).addQueryParameter("token", token);
    }

}
