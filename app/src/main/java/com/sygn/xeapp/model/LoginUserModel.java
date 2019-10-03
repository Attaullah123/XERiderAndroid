package com.sygn.xeapp.model;

public class LoginUserModel {
    private String userId;
    private String userPassword;
    private String userPhone;
    private String accessToken;
    private String accountStatus;


    public LoginUserModel(String userId, String userPassword,String userPhone, String accessToken) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.accessToken = accessToken;

    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
