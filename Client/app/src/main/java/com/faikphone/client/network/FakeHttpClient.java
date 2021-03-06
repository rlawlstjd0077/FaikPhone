package com.faikphone.client.network;

import android.content.Context;

import com.faikphone.client.data.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dsm_025 on 2017-05-11.
 */

public class FakeHttpClient extends HttpClient{
//    private String url = "http://192.168.137.253:8999/fake.do";
    private String url = "http://192.168.43.95:8080/fake.do";

    public FakeHttpClient(Context context){
        client = new OkHttpClient();
        resHandler = new ResponseHandler(context);
    }

    @Override
    public void doFakeRegister(String token, String code){
        HttpUrl.Builder urlBuilder = createBuilder("register", token);
        urlBuilder.addQueryParameter("code", code);
        final Request request = getRequest(urlBuilder, "");
        doRequest(request, true);
    }

    @Override
    public void doRealRegister(String token, String phoneNum) {
    }

    @Override
    public void doSendMessage(JSONObject msg, String token) throws JSONException {
        HttpUrl.Builder urlBuilder = createBuilder("send_message", token);
        final Request request = getRequest(urlBuilder, msg.toString());
        doRequest(request, true);
    }

    @Override
    public void doResetCode(String token) {
    }

    @Override
    public void doResetAll(String token) {
    }

    @Override
    public void doResetConnection(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("reset_conn", token);
        final Request request = getRequest(urlBuilder, "");
        doRequest(request, true);
    }

    @Override
    public HttpUrl.Builder createBuilder(String type, String token) {
        return HttpUrl.parse(url).newBuilder().addQueryParameter("type", type).addQueryParameter("token", token);
    }

    @Override
    public void doCheckConnection(String token) {
        HttpUrl.Builder urlBuilder = createBuilder("check_conn", token);
        final Request request = getRequest(urlBuilder, "");
        doRequest(request, true);
    }
}
