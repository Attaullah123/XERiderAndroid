package com.sygn.xeapp.service;


import com.sygn.xeapp.model.UserModel;

public class ServerResponse {

    private String result;
    private String message;
    private UserModel userModel;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public UserModel getUser() {
        return userModel;
    }
}
