package com.appchallengers.appchallengers.helpers.setpages;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.os.Bundle;
import com.appchallengers.appchallengers.MainActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.fragments.main.TrendsFeedFragment;
import com.appchallengers.appchallengers.fragments.main.UserFeedFragment;

/**
 * Created by MHMTNASIF on 25.02.2018.
 */

public class SetMainPages {

    public static  SetMainPages mSetMainPages;
    public static final Object OBJECT=new Object();
    private FragmentTransaction mFragmentTransaction = MainActivity.mFragmentManager.beginTransaction();
    private Activity mActivity;
    private Fragment mFragment;

    private SetMainPages(){}

    public static SetMainPages getInstance() {
        if (mSetMainPages==null){
            synchronized (OBJECT){
                if (mSetMainPages==null)
                    return new SetMainPages();
            }
        }
        return mSetMainPages;
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
                mFragment = new UserFeedFragment();
                replace("user_feed_fragment");
                break;
            } case 1: {
                mFragment = new TrendsFeedFragment();
                replace("trends_feed_fragment");
                break;
            }

        }
    }
    public void constructorWithBundle(final Activity activity, final int i,Bundle bundle) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        this.mActivity = activity;

        switch (i) {
            case 0: {
                mFragment = new UserFeedFragment();
                mFragment.setArguments(bundle);
                replace("user_feed_fragment");
                break;
            }

        }
    }

    private void replace(String tag) {

        mFragmentTransaction.replace(R.id.pager, mFragment, tag);
        mFragmentTransaction.commit();
    }
}
