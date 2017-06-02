package com.faikphone.client.network;


import android.content.Context;

import com.faikphone.client.data.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by dsm_025 on 2017-04-13.
 */

public class RealHttpClient extends HttpClient{
    private String url = "http://192.168.0.108:8080/real.do";
//    private String url = "http://10.156.145.157:9000/real.do";

    public RealHttpClient(Context context) {
        client = new OkHttpClient();
        resHandler = new ResponseHandler(context);
    }

    @Override
    public void doFakeRegister(String token, String code) {
    }

    @Override
    public void doRealRegister(String token, String phoneNum) {
        HttpUrl.Builder urlBuilder = createBuilder("register", token);
        urlBuilder.addQueryParameter("pnum", phoneNum);
        final Request request = getRequest(urlBuilder, null);
        doRequest(request, false);
    }

    @Override
    public void doSendMessage(JSONObject msg, String token) throws JSONException {
        HttpUrl.Builder urlBuilder = createBuilder("send_message", token);
        final Request request = getRequest(urlBuilder, msg.toString());
        doRequest(request, false);
    }



    @Override
    public void doResetConnection(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_conn", "");
        final Request request = getRequest(urlBuilder, "");
        doRequest(request, false);
    }

    @Override
    public void doCheckConnection(String token) {
    }

    @Override
    public void doResetCode(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_code", "");
        final Request request = getRequest(urlBuilder, null);
        doRequest(request, false);
    }

    @Override
    public void doResetAll(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_all", "");
        final Request request = getRequest(urlBuilder, null);
        doRequest(request, false);
    }


    @Override
    public HttpUrl.Builder createBuilder(String type, String token) {
        return HttpUrl.parse(url).newBuilder().addQueryParameter("type", type).addQueryParameter("token", token);
    }
}