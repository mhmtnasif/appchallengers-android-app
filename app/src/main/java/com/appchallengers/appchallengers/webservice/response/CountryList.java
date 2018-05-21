package com.appchallengers.appchallengers.webservice.response;

/**
 * Created by MHMTNASIF on 22.02.2018.
 */

public class CountryList {

    private Integer id;
    private String countryCode;
    private String countryName;

    public Integer getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
