package com.faikphone.client;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsm_025 on 2017-04-20.
 */

public class Logger {
    public void handleRealPhoneResponse(String responseJson) {
        Response response = new Gson().fromJson(responseJson, Response.class);
        if(response.isState()){
            if(response.getType().equals("register_response") || response.getType().equals("reset_code_response")){

            }else{

            }
        }else{

        }
    }

    public void handleFakePhoaneResponse(String responseJson){

    }

    public void logging(String type,String mesage){

    }
}
