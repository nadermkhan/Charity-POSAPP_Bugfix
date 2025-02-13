package com.girls.ontop.models;

public class LoginResponse {

    private String access_token;
    private String token_type;
    private String expires_in;


    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getExpiresIn() {
        return expires_in;
    }
}
