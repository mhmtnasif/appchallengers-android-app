package com.appchallengers.appchallengers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appchallengers.appchallengers.fragments.show_profil_activity_fragment.ProfilAcceptedChallangesFragment;
import com.appchallengers.appchallengers.fragments.show_profil_activity_fragment.ProfilChallangeFragment;
import com.appchallengers.appchallengers.fragments.show_profil_activity_fragment.ProfilFriendFragment;
import com.appchallengers.appchallengers.helpers.adapters.ShowProfilPagerAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.GetProfileApiClient;
import com.appchallengers.appchallengers.webservice.remote.GetProfileInfo;
import com.appchallengers.appchallengers.webservice.response.GetUserInfoResponseModel;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;

public class ShowProfilActivity extends AppCompatActivity implements View.OnClickListener, ProfilChallangeFragment.OnFragmentInteractionListener, ProfilAcceptedChallangesFragment.OnFragmentInteractionListener, ProfilFriendFragment.OnFragmentInteractionListener {

    private CircleImageView mProfilePicture;
    private ImageView mBackArrow;
    private ImageView mSettingIcon;
    private TextView mUserName;
    private TextView mUserChallengeNumber;
    private TextView mUserAcceptedChallengeNumber;
    private TextView mUserFriendsNumber;
    private SharedPreferences mSharedPreferences;
    private LinearLayout mUserInfoLinearLayout;
    private RotateLoading mRotateLoading;
    private CompositeDisposable mCompositeDisposable;
    private TabLayout mTablayout;
    private Observable<Response<GetUserInfoResponseModel>> mGetProfileInfoObservable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profil);
        initialView();
        getUserInfo();
        proccess();

    }

    private void proccess() {
        mTablayout = (TabLayout) findViewById(R.id.show_profil_tablayout);
        mTablayout.addTab(mTablayout.newTab().setText(R.string.fragment_show_profil_activity_challengers));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.fragment_show_profil_activity_accept));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.fragment_show_profil_activity_friend));
        mTablayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.show_profil_pager);
        final ShowProfilPagerAdapter showProfilPagerAdapter = new ShowProfilPagerAdapter(getSupportFragmentManager(), mTablayout.getTabCount());
        viewPager.setAdapter(showProfilPagerAdapter);
        //todo

       viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void initialView() {
        mCompositeDisposable = new CompositeDisposable();
        mRotateLoading = (RotateLoading) findViewById(R.id.activity_show_profil_info_rotateloading);
        mProfilePicture = (CircleImageView) findViewById(R.id.show_profil_activity_user_profile_picture);
        mBackArrow = (ImageView) findViewById(R.id.fragmnet_show_profil_back_button);
        mSettingIcon = (ImageView) findViewById(R.id.fragmnet_show_profil_setting_button);
        mUserName = (TextView) findViewById(R.id.show_profil_activity_user_name);
        mUserInfoLinearLayout = (LinearLayout) findViewById(R.id.profil_info);
        mUserChallengeNumber = (TextView) findViewById(R.id.show_profil_activity_challenge_number);
        mUserAcceptedChallengeNumber = (TextView) findViewById(R.id.show_profil_activity_accepted_challenge_number);
        mUserFriendsNumber = (TextView) findViewById(R.id.show_profil_activity_friends_number);
        mSharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mBackArrow.setOnClickListener(this);
        mSettingIcon.setOnClickListener(this);
    }


    public void getUserInfo() {
        GetProfileInfo getProfileInfo = GetProfileApiClient.getProfileClientWithCache(getApplicationContext());
        mGetProfileInfoObservable = getProfileInfo.getProfileInfo();
        mGetProfileInfoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<GetUserInfoResponseModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mRotateLoading.start();
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<GetUserInfoResponseModel> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            if (getUserInfoResponseModelResponse.body().getProfilepicture() != null) {
                                Picasso.with(getApplicationContext()).load(getUserInfoResponseModelResponse.body().getProfilepicture()).into(mProfilePicture);
                            }
                            mUserName.setText(getUserInfoResponseModelResponse.body().getFullname());
                            mUserChallengeNumber.setText(getUserInfoResponseModelResponse.body().getChallenges() + "");
                            mUserAcceptedChallengeNumber.setText(getUserInfoResponseModelResponse.body().getAccepted_challenges() + "");
                            mUserFriendsNumber.setText(getUserInfoResponseModelResponse.body().getFriends() + "");


                        } else {
                            if (getUserInfoResponseModelResponse.code() == 400) {
                                if (getUserInfoResponseModelResponse.errorBody() != null) {
                                    try {
                                        ErrorHandler.getInstance(getApplicationContext()).showEror(getUserInfoResponseModelResponse.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                ErrorHandler.getInstance(getApplicationContext()).showEror("{code:1000}");
                            }

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
                        mRotateLoading.stop();
                        mRotateLoading.setVisibility(View.GONE);
                        mUserInfoLinearLayout.setVisibility(View.VISIBLE);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
        if (mRotateLoading != null) {
            mRotateLoading.stop();
            mRotateLoading.setVisibility(View.GONE);
        }
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowProfilActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragmnet_show_profil_back_button: {
                startActivity(new Intent(ShowProfilActivity.this, MainActivity.class));
                finish();
                break;
            }
            case R.id.fragmnet_show_profil_setting_button: {
                startActivity(new Intent(ShowProfilActivity.this, ShowSettingUserActivity.class));
                  break;
            }
        }
    }


}