package com.appchallengers.appchallengers.webservice.response;

public class NotificationResponseModel {

    private long fromId;
    private String fromUserFullName;
    private String fromUserProfilePicture;
    private long toId;
    private int type;
    private String challengeHeadLine;
    private long actionId;

    public NotificationResponseModel(long fromId, String fromUserFullName, String fromUserProfilePicture, long toId, int type, String challengeHeadLine, long actionId) {
        this.fromId = fromId;
        this.fromUserFullName = fromUserFullName;
        this.fromUserProfilePicture = fromUserProfilePicture;
        this.toId = toId;
        this.type = type;
        this.challengeHeadLine = challengeHeadLine;
        this.actionId = actionId;
    }

    public NotificationResponseModel() {
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public String getFromUserFullName() {
        return fromUserFullName;
    }

    public void setFromUserFullName(String fromUserFullName) {
        this.fromUserFullName = fromUserFullName;
    }

    public String getFromUserProfilePicture() {
        return fromUserProfilePicture;
    }

    public void setFromUserProfilePicture(String fromUserProfilePicture) {
        this.fromUserProfilePicture = fromUserProfilePicture;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChallengeHeadLine() {
        return challengeHeadLine;
    }

    public void setChallengeHeadLine(String challengeHeadLine) {
        this.challengeHeadLine = challengeHeadLine;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
