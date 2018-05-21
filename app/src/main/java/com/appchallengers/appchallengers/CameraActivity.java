package com.appchallengers.appchallengers;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.appchallengers.appchallengers.helpers.setpages.SetCameraPages;
import com.appchallengers.appchallengers.helpers.util.ConnectivityReceiver;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.MyApplication;

public class CameraActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    public static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mFragmentManager = getSupportFragmentManager();
        if (!getIntent().hasExtra("challengeId")) {
            finish();
        } else {
            Bundle bundle = getIntent().getExtras();
            SetCameraPages.getInstance().constructorWithBundle(CameraActivity.this, 2, bundle);
        }
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
