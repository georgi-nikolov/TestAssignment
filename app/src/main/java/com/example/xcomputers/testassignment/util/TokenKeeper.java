package com.example.xcomputers.testassignment.util;

/**
 * Created by Georgi on 4/20/2017.
 */

/**
 * Used to store the access token and give it to the ApiClient
 */
public class TokenKeeper {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
