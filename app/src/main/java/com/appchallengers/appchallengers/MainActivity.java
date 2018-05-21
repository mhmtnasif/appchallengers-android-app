package com.appchallengers.appchallengers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appchallengers.appchallengers.helpers.setpages.SetMainPages;
import com.appchallengers.appchallengers.helpers.util.ConnectivityReceiver;
import com.appchallengers.appchallengers.helpers.util.Constants;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.MyApplication;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserNotification;
import com.appchallengers.appchallengers.webservice.remote.UserNotificationApiClient;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import android.widget.EditText;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , ConnectivityReceiver.ConnectivityReceiverListener,View.OnClickListener{
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mActive;
    private int mControl;
    private String mProfileImageUrl;
    private ImageView mProfileImageView;
    private EditText mSearchEditText;
    private List<UserBaseDataModel> mResponsUserBase;
    private ImageView mNotificationImageview;
    public static FragmentManager mFragmentManager;
    private BottomNavigationView mBottomNavigationView;
    private static final String TAG = "MainActivity";
    private Observable<Response<Void>> hasNotificationObservable;
    private CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.contex=getApplicationContext();
        mFragmentManager = getSupportFragmentManager();
        initialView();
        checkAuthentication();
        if (!getIntent().hasExtra("status")) {
            SetMainPages.getInstance().constructor(MainActivity.this, 0);
        } else {
            Bundle bundle = getIntent().getExtras();
            SetMainPages.getInstance().constructorWithBundle(MainActivity.this, 0, bundle);
        }

    }

    private void checkAuthentication(){
        if (mToken == null|| mToken.equals("")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 0);
            finish();
        } else {
            if ( mActive.equals("0")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 0);
                finish();
            }else{
                mBottomNavigationView.getMenu().getItem(0).setCheckable(true);
                SetMainPages.getInstance().constructor(MainActivity.this, 0);
            }
        }

    }

    private void initialView() {
        compositeDisposable = new CompositeDisposable();
        mSharedPreferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Utils.sharedPreferences = mSharedPreferences;
        mToken = Utils.getPref("token");
        mActive = Utils.getPref("active");
        mProfileImageUrl=Utils.getPref("imageUrl");
        mProfileImageView=(ImageView)findViewById(R.id.main_activity_user_profile_picture);
        mNotificationImageview=(ImageView)findViewById(R.id.mani_activity_notification_imageview);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mSearchEditText=(EditText) findViewById(R.id.search_main_activity_edittext);
        if (mProfileImageUrl!=null){
            Picasso.with(this).load(mProfileImageUrl).into(mProfileImageView);
        }
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mNotificationImageview.setOnClickListener(this);
        mSearchEditText.setOnClickListener(this);
        mProfileImageView.setOnClickListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.action_add_challenge:
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                long challengeId = -999;
                intent.putExtra("challengeId", challengeId);
                startActivity(intent);
                break;
            case R.id.action_user_feed:{
                if (mFragmentManager != null&&mControl!=0){
                    SetMainPages.getInstance().constructor(MainActivity.this, 0);
                }
                mControl=0;
                break;
            }

            case R.id.action_trends_feed:
                if (mFragmentManager!=null&&mControl!=1)
                    SetMainPages.getInstance().constructor(MainActivity.this,1);
                mControl=1;
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mani_activity_notification_imageview:{
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                break;
            }
            case R.id.main_activity_user_profile_picture:{
                startActivity(new Intent(MainActivity.this,ShowProfilActivity.class));
                finish();
                break;
            }
            case  R.id.search_main_activity_edittext:{
                startActivity(new Intent(MainActivity.this,ShowSearchActivity.class));
                break;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //hashNotification();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void hashNotification() {
        UserNotification userNotification = UserNotificationApiClient.getUserChallengesClient(getBaseContext());
        hasNotificationObservable = userNotification.hasNotification();
        hasNotificationObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> value) {
                        if (value.isSuccessful()) {
                            mNotificationImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_orange_24dp));
                        } else if (value.code() == 404) {
                            mNotificationImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_none_black_24dp));

                        } else {
                            ErrorHandler.getInstance(getApplicationContext()).showEror("{code:1000}");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getApplicationContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getApplicationContext()).showEror("{code:1000}");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.pager);
        InternetControl.getInstance().showSnackGeneral(frameLayout, isConnected);
    }


}

