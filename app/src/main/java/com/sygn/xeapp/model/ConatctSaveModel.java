package com.sygn.xeapp.model;

public class ConatctSaveModel {
    private String useNickName;
    private String userName;
    private String userNumber;

    public ConatctSaveModel(String useNickName, String userName, String userNumber) {
        this.useNickName = useNickName;
        this.userName = userName;
        this.userNumber = userNumber;
    }

    public String getUseNickName() {
        return useNickName;
    }

    public void setUseNickName(String useNickName) {
        this.useNickName = useNickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
