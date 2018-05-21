package com.appchallengers.appchallengers;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;


import com.appchallengers.appchallengers.fragments.login.LoginFragment;
import com.appchallengers.appchallengers.fragments.login.SignUpFragment;
import com.appchallengers.appchallengers.helpers.setpages.SetLoginPages;
import com.appchallengers.appchallengers.helpers.util.ConnectivityReceiver;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.MyApplication;
import com.appchallengers.appchallengers.helpers.util.Utils;

import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;

public class LoginActivity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {

    public static FragmentManager mFragmentManager;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFragmentManager = getSupportFragmentManager();
        mSharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Utils.sharedPreferences = mSharedPreferences;


        boolean control = Utils.getPrefBoolean("firstrun");
        if (control) {
            SetLoginPages.getInstance().constructor(LoginActivity.this, 0);
        } else {
            SetLoginPages.getInstance().constructor(LoginActivity.this, 1);
        }
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = LoginActivity.mFragmentManager.findFragmentById(R.id.pager);
        if (currentFragment instanceof SignUpFragment){
            SetLoginPages.getInstance().constructor(LoginActivity.this, 1);
        }
        else if (currentFragment instanceof LoginFragment){
            SetLoginPages.getInstance().constructor(LoginActivity.this, 1);
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        FrameLayout frameLayout=(FrameLayout) findViewById(R.id.pager);
        InternetControl.getInstance().showSnackGeneral(frameLayout,isConnected);
    }


}
