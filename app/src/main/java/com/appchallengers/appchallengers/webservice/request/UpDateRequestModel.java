package com.appchallengers.appchallengers.webservice.request;

/**
 * Created by jir on 17.2.2018.
 */

public class UpDateRequestModel {
    private String fullName;
    private String country;

    public UpDateRequestModel(String fullName, String country) {
        this.fullName = fullName;
        this.country = country;
    }
}
