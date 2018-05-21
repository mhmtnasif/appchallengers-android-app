package com.appchallengers.appchallengers.helpers.setpages;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appchallengers.appchallengers.LoginActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.fragments.login.ConfirmEmailFragment;
import com.appchallengers.appchallengers.fragments.login.IntroFragment;
import com.appchallengers.appchallengers.fragments.login.LoginFragment;
import com.appchallengers.appchallengers.fragments.login.SignUpFragment;
import com.appchallengers.appchallengers.fragments.login.SplashFragment;

/**
 * Created by MHMTNASIF on 13.02.2018.
 */

public class SetLoginPages {

    public static  SetLoginPages mSetLoginPages;
    public static final Object OBJECT=new Object();
    private FragmentTransaction mFragmentTransaction = LoginActivity.mFragmentManager.beginTransaction();
    private Activity mActivity;
    private Fragment mFragment;

    private SetLoginPages(){}

    public static SetLoginPages getInstance() {
        if (mSetLoginPages==null){
            synchronized (OBJECT){
                if (mSetLoginPages==null)
                    return new SetLoginPages();
            }
        }
        return mSetLoginPages;
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
                mFragment = new SplashFragment();
                replace("splash");
                break;
            } case 1: {
                mFragment = new IntroFragment();
                replace("intro");
                break;
            }
            case 2: {
                mFragment = new LoginFragment();
                replace("login");
                break;
            }
            case 3: {
                mFragment = new SignUpFragment();
                replace("signup");
                break;
            }
            case 4: {
                mFragment = new ConfirmEmailFragment();
                replace("confirm_email");
                break;
            }
        }
    }

    private void replace(String tag) {

        mFragmentTransaction.replace(R.id.pager, mFragment, tag);
        mFragmentTransaction.commit();
    }
}
