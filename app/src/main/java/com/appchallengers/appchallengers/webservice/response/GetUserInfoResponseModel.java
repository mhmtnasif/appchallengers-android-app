package com.appchallengers.appchallengers.webservice.response;

/**
 * Created by MHMTNASIF on 22.03.2018.
 */

public class GetUserInfoResponseModel {

    private long id;
    private String fullname;
    private String profilepicture;
    private String country;
    private long useractionid;
    private int status;
    private long challenges;
    private long accepted_challenges;
    private long friends;

    public GetUserInfoResponseModel(long id, String fullname, String profilepicture, String country, long useractionid, int status, long challenges, long accepted_challenges, long friends) {
        this.id = id;
        this.fullname = fullname;
        this.profilepicture = profilepicture;
        this.country = country;
        this.useractionid = useractionid;
        this.status = status;
        this.challenges = challenges;
        this.accepted_challenges = accepted_challenges;
        this.friends = friends;
    }

    public GetUserInfoResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getUseractionid() {
        return useractionid;
    }

    public void setUseractionid(long useractionid) {
        this.useractionid = useractionid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getChallenges() {
        return challenges;
    }

    public void setChallenges(long challenges) {
        this.challenges = challenges;
    }

    public long getAccepted_challenges() {
        return accepted_challenges;
    }

    public void setAccepted_challenges(long accepted_challenges) {
        this.accepted_challenges = accepted_challenges;
    }

    public long getFriends() {
        return friends;
    }

    public void setFriends(long friends) {
        this.friends = friends;
    }
}

