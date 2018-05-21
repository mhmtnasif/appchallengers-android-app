package com.appchallengers.appchallengers.helpers.setpages;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowSettingUserActivity;
import com.appchallengers.appchallengers.fragments.login.ConfirmEmailFragment;
import com.appchallengers.appchallengers.fragments.login.IntroFragment;
import com.appchallengers.appchallengers.fragments.login.LoginFragment;
import com.appchallengers.appchallengers.fragments.login.SignUpFragment;
import com.appchallengers.appchallengers.fragments.login.SplashFragment;
import com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment.SettingEmailChangeFragment;
import com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment.SettingFragment;
import com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment.SettingPasswordChangeFragment;
import com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment.SettingProfileFragment;

/**
 * Created by MHMTNASIF on 13.02.2018.
 */

public class SetSettingPages {

    public static SetSettingPages setSettingPages;
    public static final Object OBJECT=new Object();
    private FragmentTransaction mFragmentTransaction = ShowSettingUserActivity.mFragmentManager.beginTransaction();
    private Activity mActivity;
    public static boolean  mControl=false;
    private Fragment mFragment;

    private SetSettingPages(){}

    public static SetSettingPages getInstance() {
        if (setSettingPages ==null){
            synchronized (OBJECT){
                if (setSettingPages ==null)
                    return new SetSettingPages();
            }
        }
        return setSettingPages;
    }
    public static boolean getFragmentControl(){
        return mControl;
    }
    public static boolean setFragmentControl(boolean control){
        mControl=control;
        return mControl;
    }
    public void constructor(final Activity activity, final int i) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        this.mActivity = activity;
        mControl=true;
        switch (i) {
            case 0: {
                mFragment = new SettingFragment();
                replace("main");
                break;
            } case 1: {
                mFragment = new SettingProfileFragment();
                replace("profile");
                break;
            }
            case 2: {
                mFragment = new SettingPasswordChangeFragment();
                replace("password");
                break;
            }
            case 3: {
                mFragment = new SettingEmailChangeFragment();
                replace("email");
                break;
            }
        }
    }

    private void replace(String tag) {

        mFragmentTransaction.replace(R.id.activity_setting_framelayout, mFragment, tag);
        mFragmentTransaction.commit();
    }
}
