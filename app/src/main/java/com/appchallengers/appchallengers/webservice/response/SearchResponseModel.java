package com.appchallengers.appchallengers.webservice.response;

import java.util.List;

public class SearchResponseModel {

    private List<UserBaseDataModel> userList;
    private List<SearchChallengesResponseModel> challengesList;


    public SearchResponseModel(List<UserBaseDataModel> userList, List<SearchChallengesResponseModel> challengesList) {
        this.userList = userList;
        this.challengesList = challengesList;
    }

    public SearchResponseModel() {
    }

    public List<UserBaseDataModel> getUserList() {
        return userList;
    }

    public void setUserList(List<UserBaseDataModel> userList) {
        this.userList = userList;
    }

    public List<SearchChallengesResponseModel> getChallengesList() {
        return challengesList;
    }

    public void setChallengesList(List<SearchChallengesResponseModel> challengesList) {
        this.challengesList = challengesList;
    }
}
