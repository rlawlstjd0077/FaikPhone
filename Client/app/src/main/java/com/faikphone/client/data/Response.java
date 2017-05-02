package com.faikphone.client.data;

/**
 * Created by dsm_025 on 2017-04-25.
 */

public class Response {
    private boolean state;
    private String type;
    private String message;

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
