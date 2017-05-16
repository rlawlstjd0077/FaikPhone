package server.manager;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by dsm_025 on 2017-05-15.
 */
public class HttpClientManager {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "key=AAAAWGtqijs:APA91bEj253aBLOCV3hFtYl7umYBJlmDryas8xh3yhR-SPH8uXepDQwZKAlRJqw9hmuW26h4bKq-sWKN_S0ZzkVRo4FVB52ZWJtZjMRtfm4XkqGt-pTZGrjSudASFUJ7N0p6Y9EjZoU9";
    private String token;

    /**
     * 상대방의 Token
     * @param token
     */
    public HttpClientManager(String token){
        this.token = token;
    }

    public boolean sendCall(String name, String number){
        return doRequest(createBodyData(name, number, "message").toString());
    }

    public boolean sendMessage(String name, String number, String content){
        return doRequest(createBodyData(name, number,content, "message").toString());
    }

    public boolean doRequest(String bodyData){
        final OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, bodyData.toString());
        final Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .header("Authorization", SERVER_KEY)
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("response : " + response);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private JSONObject createBodyData(String name, String number, String type){
        JSONObject object = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            object.put("to", token);
            data.put("type", type);
            data.put("name",  name);
            data.put("number", number);
            data.put("score","5x1");
            data.put("time", "9:50");
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject createBodyData(String name, String number, String content, String type){
        JSONObject object = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            object.put("to", token);
            data.put("type", type);
            data.put("name",  name);
            data.put("number", number);
            data.put("content", content);
            data.put("score","5x1");
            data.put("time", "9:50");
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}

