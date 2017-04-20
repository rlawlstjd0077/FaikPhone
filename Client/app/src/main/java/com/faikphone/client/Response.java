package com.faikphone.client;

/**
 * Created by dsm_025 on 2017-04-20.
 */
public class Response {
    private String type;
    private boolean state;
    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
