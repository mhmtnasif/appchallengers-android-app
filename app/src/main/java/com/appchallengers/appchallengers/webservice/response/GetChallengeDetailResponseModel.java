package com.appchallengers.appchallengers.webservice.response;

public class GetChallengeDetailResponseModel {

    private String headLine;
    private long counter;
    private int status;
    private long challenge_id;

    public GetChallengeDetailResponseModel(String headLine, long counter, int status, long challenge_id) {
        this.headLine = headLine;
        this.counter = counter;
        this.status = status;
        this.challenge_id = challenge_id;
    }

    public GetChallengeDetailResponseModel() {
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(long challenge_id) {
        this.challenge_id = challenge_id;
    }
}
