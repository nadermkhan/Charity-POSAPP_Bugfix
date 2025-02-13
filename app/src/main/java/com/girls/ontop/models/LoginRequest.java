package com.girls.ontop.models;

public class LoginRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;
    private String scope;

    public LoginRequest(String username, String password) {
        this.grant_type = "password";
        this.client_id = "15";
        this.client_secret = "LwYlNcKRAuvH9YOCifiQjJLvrm2J4hT42MU2cMaj";
        this.username = username;
        this.password = password;
        this.scope = "";
    }
}
