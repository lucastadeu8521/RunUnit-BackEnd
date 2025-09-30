package com.rununit.rununit.dto;

public class JwtResponse {
    private String token;
    private String email;
    private String name;

    public JwtResponse(String token, String email, String name) {
        this.token = token;
        this.email = email;
        this.name = name;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getName() { return name; }
}
