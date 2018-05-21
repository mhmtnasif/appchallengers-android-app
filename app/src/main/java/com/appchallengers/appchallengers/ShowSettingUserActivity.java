package com.appchallengers.appchallengers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.appchallengers.appchallengers.helpers.setpages.SetSettingPages;
import com.appchallengers.appchallengers.helpers.util.Utils;

import io.reactivex.disposables.CompositeDisposable;

public class ShowSettingUserActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBackImage;
    public static FragmentManager mFragmentManager;
    private CompositeDisposable mCompositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_setting_user);
        mBackImage=(ImageView) findViewById(R.id.activity_setting_back_Image);
        mBackImage.setOnClickListener(this);
        mCompositeDisposable=new CompositeDisposable();
        mFragmentManager = getSupportFragmentManager();

        SetSettingPages.getInstance().constructor(ShowSettingUserActivity.this, 0);

    }


    @Override
    public void onBackPressed() {
            SetSettingPages.getInstance().constructor(ShowSettingUserActivity.this, 0);
            super.onBackPressed();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_setting_back_Image: {
                 if (SetSettingPages.getFragmentControl()){
                     SetSettingPages.getInstance().constructor(ShowSettingUserActivity.this, 0);
                     SetSettingPages.setFragmentControl(false);
                 }
                 else {
                     startActivity(new Intent(getApplicationContext(), ShowProfilActivity.class));
                     finish();
                 }
                break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
