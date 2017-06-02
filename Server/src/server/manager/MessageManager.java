package server.manager;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by dsm_025 on 2017-04-08.
 */
public class MessageManager {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "key=AAAAWGtqijs:APA91bEj253aBLOCV3hFtYl7umYBJlmDryas8xh3yhR-SPH8uXepDQwZKAlRJqw9hmuW26h4bKq-sWKN_S0ZzkVRo4FVB52ZWJtZjMRtfm4XkqGt-pTZGrjSudASFUJ7N0p6Y9EjZoU9";
    private String token;

    /**
     * 상대방의 Token
     * @param token
     */
    public MessageManager(String token){
        this.token = token;
    }

    public boolean sendMessage(String jsonData){
        return doRequest(createBodyData(jsonData).toString());
    }

    public boolean doRequest(String bodyData){
        final OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, bodyData);
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

    private JSONObject createBodyData(String json){
        JSONObject object = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            object.put("to", token);
            data.put("json", json);
            data.put("score","5x1");
            data.put("time", "9:50");
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
