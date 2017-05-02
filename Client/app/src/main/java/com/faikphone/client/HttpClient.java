package com.faikphone.client;


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

public class HttpClient {
    private String url = "http://192.168.137.115:8999/";
    private boolean state;
    private final OkHttpClient client;
    private Requester requester;
    private Logger logger;
    private ResponseHandler resHandler;

    public HttpClient(boolean state) {
        url += state ? "real.do" : "fake.do";
        client = new OkHttpClient();
        this.state = state;
        logger = Logger.getInstance();
        resHandler = new ResponseHandler();
    }

    public void doRegister(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("register", token);
        final Request request = getRequest(urlBuilder);
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

    public String doSendMessage(String msg, String token){
        HttpUrl.Builder urlBuilder = createBuilder("send_message", token);
        urlBuilder.addQueryParameter("message", msg);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    public String doResetConnection(String token){
        HttpUrl.Builder urlBuilder = createBuilder("reset_conn", token);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    public String doResetCode(String token){
        if(state) {
            HttpUrl.Builder urlBuilder = createBuilder("reset_code", token);
            final Request request = getRequest(urlBuilder);
            return null;
        }
        return null;
    }

    public String doResetAll(String token){
        HttpUrl.Builder urlBuilder = createBuilder("reset_all", token);
        final Request request = getRequest(urlBuilder);
        return null;
    }

    public HttpUrl.Builder createBuilder(String type, String token){
        return  HttpUrl.parse(url).newBuilder().addQueryParameter("type", type).addQueryParameter("token", token);
    }

    public Request getRequest(HttpUrl.Builder builder){
        return new Request.Builder()
                .url(builder.build().toString())
                .get()
                .build();
    }


}
