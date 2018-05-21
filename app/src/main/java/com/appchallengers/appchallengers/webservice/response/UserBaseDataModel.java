package com.appchallengers.appchallengers.webservice.response;

/**
 * Created by MHMTNASIF on 19.03.2018.
 */

public class UserBaseDataModel {

    private long id;
    private String fullName;
    private String profile_picture;

    public UserBaseDataModel(long id, String fullName, String profile_picture) {
        this.id = id;
        this.fullName = fullName;
        this.profile_picture = profile_picture;
    }

    public UserBaseDataModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
