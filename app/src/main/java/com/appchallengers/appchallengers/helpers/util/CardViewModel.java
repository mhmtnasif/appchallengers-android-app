package com.appchallengers.appchallengers.helpers.util;

/**
 * Created by jir on 10.3.2018.
 */
import android.widget.VideoView;

public class CardViewModel {
    private int imageview;
    private VideoView videoView;
    private String like;
    private String dislike;
    private String fullname;
    private String headline;
    public  CardViewModel(){

    }

    public CardViewModel(int imageview, VideoView videoView, String like, String dislike, String fullname, String headline) {
        this.imageview = imageview;
        this.videoView = videoView;
        this.like = like;
        this.dislike = dislike;
        this.fullname = fullname;
        this.headline = headline;
    }

    public int getImageview() {
        return imageview;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public String getLike() {
        return like;
    }

    public String getDislike() {
        return dislike;
    }

    public String getFullname() {
        return fullname;
    }

    public String getHeadline() {
        return headline;
    }

    public void setImageview(int imageview) {
        this.imageview = imageview;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
