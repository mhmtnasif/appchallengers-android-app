package com.appchallengers.appchallengers.fragments.camera;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.setpages.SetCameraPages;

import java.io.File;

public class VideoPlayerFragment extends Fragment implements MediaPlayer.OnPreparedListener,
        View.OnTouchListener, View.OnClickListener {

    private View mRootView;
    private VideoView mVideoPlayerVideoView;
    private int mVideoPlayerCurrentPosition;
    private ImageView mVideoPlayerClose;
    private String mPath;
    private Button mChallengeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mVideoPlayerVideoView = (VideoView) mRootView.findViewById(R.id.video_player_fragment_videoview);
        mVideoPlayerClose = (ImageView) mRootView.findViewById(R.id.video_player_fragment_close);
        mChallengeButton = (Button) mRootView.findViewById(R.id.video_player_fragment_challenge_button);
        mChallengeButton.setOnClickListener(this);
        mVideoPlayerVideoView.setOnTouchListener(this);
        mVideoPlayerClose.setOnClickListener(this);
        initialVideoPlayer();
    }

    private void initialVideoPlayer() {
        Bundle bundle = getArguments();
        mPath = bundle.getString("path");
        if (mPath.equals(null) || mPath.equals("")) {
            getActivity().finish();
        } else {
            mVideoPlayerVideoView.setVideoURI(Uri.parse(mPath));
            mVideoPlayerVideoView.setOnPreparedListener(this);
            mVideoPlayerVideoView.requestFocus();
            mVideoPlayerVideoView.start();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mVideoPlayerVideoView.isPlaying()) {
            mVideoPlayerVideoView.pause();
            mVideoPlayerCurrentPosition = mVideoPlayerVideoView.getCurrentPosition();
            return false;
        } else {
            mVideoPlayerVideoView.seekTo(mVideoPlayerCurrentPosition);
            mVideoPlayerVideoView.start();
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoPlayerVideoView.pause();
        mVideoPlayerCurrentPosition = mVideoPlayerVideoView.getCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoPlayerVideoView.seekTo(mVideoPlayerCurrentPosition);
        mVideoPlayerVideoView.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_player_fragment_close: {
                deleteVideoFile();
                getActivity().finish();
                break;
            }
            case R.id.video_player_fragment_challenge_button: {
                Bundle get=getArguments();
                SetCameraPages.getInstance().constructorWithBundle(getActivity(),1,get);
                break;
            }
        }
    }

    public boolean deleteVideoFile() {
        File file = new File(mPath);
        return file.delete();
    }

}
