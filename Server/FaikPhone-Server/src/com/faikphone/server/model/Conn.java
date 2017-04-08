package com.faikphone.server.model;

/**
 * Created by dsm_025 on 2017-04-04.
 */
public class Conn {
    private String realToken;
    private String fakeToken;
    private String code;

    public Conn(String realToken, String code, String fakeToken) {
        this.realToken = realToken;
        this.fakeToken = fakeToken;
        this.code = code;
    }

    public String getRealToken() {
        return realToken;
    }
    public String getFakeToken() {
        return fakeToken;
    }
    public String getCode() {
        return code;
    }

    public void setFakeToken(String fakeToken) {
        this.fakeToken = fakeToken;
    }
    public void setRealToken(String realToken) {
        this.realToken = realToken;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
