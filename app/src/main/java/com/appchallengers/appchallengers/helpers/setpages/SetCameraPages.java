package com.appchallengers.appchallengers.helpers.setpages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appchallengers.appchallengers.CameraActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.fragments.camera.CaptureVideoFragment;
import com.appchallengers.appchallengers.fragments.camera.SelectFriendsFragment;
import com.appchallengers.appchallengers.fragments.camera.VideoPlayerFragment;

/**
 * Created by MHMTNASIF on 25.02.2018.
 */

public class SetCameraPages {

    public static SetCameraPages mSetCameraPages;
    public static final Object OBJECT=new Object();
    private FragmentTransaction mFragmentTransaction = CameraActivity.mFragmentManager.beginTransaction();
    private Activity mActivity;
    private Fragment mFragment;

    private SetCameraPages(){}

    public static SetCameraPages getInstance() {
        if (mSetCameraPages==null){
            synchronized (OBJECT){
                if (mSetCameraPages==null)
                    return new SetCameraPages();
            }
        }
        return mSetCameraPages;
    }
    public void constructor(final Activity activity, final int i) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        this.mActivity = activity;

        switch (i) {
            case 0: {
                mFragment = CaptureVideoFragment.newInstance();
                replace("capture_video_fragment");
                break;
            }

        }
    }
    public void constructorWithBundle(final Activity activity, final int i, Bundle bundle) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        this.mActivity = activity;

        switch (i) {
            case 0: {
                mFragment = new VideoPlayerFragment();
                mFragment.setArguments(bundle);
                replace("video_player_fragment");
                break;
            }
            case 1: {
                mFragment = new SelectFriendsFragment();
                mFragment.setArguments(bundle);
                replace("select_friends_fragment");
                break;
            }
            case 2: {
                mFragment = CaptureVideoFragment.newInstance();
                mFragment.setArguments(bundle);
                replace("capture_video_fragment");
                break;
            }
        }
    }

    private void replace(String tag) {
        mFragmentTransaction.replace(R.id.pager, mFragment, tag);
        mFragmentTransaction.commit();
    }
}
