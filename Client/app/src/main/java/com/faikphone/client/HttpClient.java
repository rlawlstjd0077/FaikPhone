package com.faikphone.client;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dsm_025 on 2017-04-13.
 */

public class HttpClient {
    private String url = "http://10.156.145.193:8999/";
    private boolean state;
    private final OkHttpClient client;

    public HttpClient(boolean state) {
        url += state ? "real.do" : "fake.do";
        client = new OkHttpClient();
        this.state = state;
    }

    public void doRegister(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("register", token);
        final Request request = getRequest(urlBuilder);
        Response response = doRequest(request);
    }

    public void doSendMessage(String msg, String token){
        HttpUrl.Builder urlBuilder = createBuilder("send_message", token);
        urlBuilder.addQueryParameter("message", msg);
        final Request request = getRequest(urlBuilder);
        doRequest(request);
    }

    public void doResetConnection(String token){
        HttpUrl.Builder urlBuilder = createBuilder("reset_conn", token);
        final Request request = getRequest(urlBuilder);
        doRequest(request);
    }

    public void doResetCode(String token){
        if(state) {
            HttpUrl.Builder urlBuilder = createBuilder("reset_code", token);
            final Request request = getRequest(urlBuilder);
            doRequest(request);
        }
    }

    public void doResetAll(String token){
        HttpUrl.Builder urlBuilder = createBuilder("reset_all", token);
        final Request request = getRequest(urlBuilder);
        doRequest(request);
    }

    public HttpUrl.Builder createBuilder(String type, String token){
        return  HttpUrl.parse(url).newBuilder();
    }

    public Request getRequest(HttpUrl.Builder builder){
        return new Request.Builder()
                .url(builder.build().toString())
                .get()
                .build();
    }

    public Response doRequest(final Request req) {
        final Response[] response = new Response[1];
        try {
            new Thread(new Runnable() {
                @Override
                public void run () {
                    try {
                        response[0] = client.newCall(req).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response[0];
    }
}
