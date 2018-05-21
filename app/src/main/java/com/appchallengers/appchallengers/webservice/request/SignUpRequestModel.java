package com.appchallengers.appchallengers.webservice.request;

/**
 * Created by jir on 17.2.2018.
 */

public class SignUpRequestModel {
    private String fullName;
    private String email;
    private String password;
    private String country;

    public SignUpRequestModel(String fullName, String email, String password, String country) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.country = country;
    }
}
