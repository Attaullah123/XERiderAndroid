package com.sygn.xeapp.service;


import com.sygn.xeapp.model.UserModel;

public class ServerRequest {

    private String operation;
    private UserModel userModel;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(UserModel userModel) {
        this.userModel = userModel;
    }
}
