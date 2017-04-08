package com.faikphone.server;

import org.json.JSONObject;

/**
 * Created by dsm_025 on 2017-04-06.
 */
public class Util {
    public static String makeSuccessResponse(String message){
        JSONObject object = new JSONObject();
        object.put("type", "success");
        object.put("message", message);
        return object.toString();
    }
    public static String makeErrorResponse(String message){
        JSONObject object = new JSONObject();
        object.put("type", "error");
        object.put("message", message);
        return object.toString();
    }
}
