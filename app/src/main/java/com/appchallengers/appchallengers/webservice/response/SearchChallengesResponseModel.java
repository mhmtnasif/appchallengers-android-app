package com.appchallengers.appchallengers.webservice.response;

public class SearchChallengesResponseModel {

    private long challengeId;
    private String headLine;

    public SearchChallengesResponseModel(long challengeId, String headLine) {
        this.challengeId = challengeId;
        this.headLine = headLine;
    }

    public SearchChallengesResponseModel() {
    }

    public long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }
}
