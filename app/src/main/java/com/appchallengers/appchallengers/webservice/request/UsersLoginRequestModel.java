package com.appchallengers.appchallengers.webservice.request;

/**
 * Created by jir on 18.2.2018.
 */

public class UsersLoginRequestModel  {
    private String email;
    private String password;

    public UsersLoginRequestModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
