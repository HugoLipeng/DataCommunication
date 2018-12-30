package com.xray.loaderdemo;

/**
 * 自定义实体类
 */
public class UserBean {

    private String userName;
    private String password;

    public UserBean(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
