package com.appchallengers.appchallengers;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.UserAcceptedChallengesFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.UserFriendFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.User_ChallengeFragment;
import com.appchallengers.appchallengers.helpers.adapters.ShowUserPagerAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.GetUserToken;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.GetUserInfo;
import com.appchallengers.appchallengers.webservice.remote.GetUserInfoApiClient;
import com.appchallengers.appchallengers.webservice.remote.UserRelationship;
import com.appchallengers.appchallengers.webservice.remote.UserRelationshipApiClient;
import com.appchallengers.appchallengers.webservice.response.GetUserInfoResponseModel;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ShowUserActivity extends AppCompatActivity implements View.OnClickListener, User_ChallengeFragment.OnFragmentInteractionListener, UserAcceptedChallengesFragment.OnFragmentInteractionListener, UserFriendFragment.OnFragmentInteractionListener {

    private Bundle bundle;
    private long mUserId;
    private long mHostId;
    private CircleImageView mProfilePicture;
    private int mButtonStatus;
    private ImageView mBackArrow;
    private TextView mUserName;
    private TextView mUserChallengeNumber;
    private TextView mUserAcceptedChallengeNumber;
    private TextView mUserFriendsNumber;
    private Button mRelationshipButton;
    private LinearLayout mUserInfoLinearLayout;
    private RotateLoading mRotateLoading;
    private CompositeDisposable mCompositeDisposable;
    private TabLayout mTablayout;
    private Observable<Response<GetUserInfoResponseModel>> mGetUserInfoObservable;
    private Observable<Response<Void>> mAddFirend;
    private Observable<Response<Void>> mAcceptFirend;
    private Observable<Response<Void>> mDeleteFirend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        if (!getIntent().hasExtra("user_id")) {
            finish();
        } else {
            bundle = getIntent().getExtras();
            mUserId = bundle.getLong("user_id");
            initialView();
            getUserInfo();
            proccess();
            String token = GetUserToken.getToken(getApplicationContext());
            try {
                mHostId = Utils.getIdFromToken(token);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void proccess() {
        mTablayout = (TabLayout) findViewById(R.id.show_user_tablayout);
        mTablayout.addTab(mTablayout.newTab().setText(R.string.fragment_show_profil_activity_challengers));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.fragment_show_profil_activity_accept));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.fragment_show_profil_activity_friend));
        mTablayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.show_user_pager);
        final ShowUserPagerAdapter showUserPagerAdapter = new ShowUserPagerAdapter(getSupportFragmentManager(), mTablayout.getTabCount(), bundle);
        viewPager.setAdapter(showUserPagerAdapter);
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
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initialView() {
        mCompositeDisposable = new CompositeDisposable();
        mRotateLoading = (RotateLoading) findViewById(R.id.activity_show_user_info_rotateloading);
        mProfilePicture = (CircleImageView) findViewById(R.id.show_user_activity_user_profile_picture);
        mBackArrow = (ImageView) findViewById(R.id.fragmnet_show_user_back_button);
        mUserName = (TextView) findViewById(R.id.show_user_activity_user_name);
        mUserInfoLinearLayout = (LinearLayout) findViewById(R.id.user_info);
        mUserChallengeNumber = (TextView) findViewById(R.id.show_user_activity_challenge_number);
        mUserAcceptedChallengeNumber = (TextView) findViewById(R.id.show_user_activity_accepted_challenge_number);
        mUserFriendsNumber = (TextView) findViewById(R.id.show_user_activity_friends_number);
        mRelationshipButton = (Button) findViewById(R.id.show_user_activity_relationship_button);
        mRelationshipButton.setOnClickListener(this);
    }


    public void getUserInfo() {
        GetUserInfo getUserInfo = GetUserInfoApiClient.getUserInfoClientWithCache(getApplicationContext());
        mGetUserInfoObservable = getUserInfo.getUserInfo(mUserId);
        mGetUserInfoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<GetUserInfoResponseModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mRotateLoading.start();
                    }

                    @Override
                    public void onNext(Response<GetUserInfoResponseModel> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            mButtonStatus = -1;
                            if(getUserInfoResponseModelResponse.body().getProfilepicture()!=null){
                                Picasso.with(getApplicationContext()).load(getUserInfoResponseModelResponse.body().getProfilepicture()).into(mProfilePicture);
                            }
                            mUserName.setText(getUserInfoResponseModelResponse.body().getFullname());
                            mUserChallengeNumber.setText(getUserInfoResponseModelResponse.body().getChallenges() + "");
                            mUserAcceptedChallengeNumber.setText(getUserInfoResponseModelResponse.body().getAccepted_challenges() + "");
                            mUserFriendsNumber.setText(getUserInfoResponseModelResponse.body().getFriends() + "");
                            relationship(getUserInfoResponseModelResponse.body().getUseractionid(), getUserInfoResponseModelResponse.body().getStatus());


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

    private void relationship(long useractionid, int status) {
        if (useractionid == mHostId) {
            if (status == 1) {
                mRelationshipButton.setText("Arkadaşlıktan cıkar");
                mButtonStatus = 0;
            } else if (status == -1) {
                mRelationshipButton.setText("arkadaş ekle");
                mButtonStatus = 1;
            } else if (status == 0) {
                mRelationshipButton.setText("isteği iptal et");
                mButtonStatus = 2;
            }
        } else {
            if (status == 1) {
                mRelationshipButton.setText("Arkadaşlıktan cıkar");
                mButtonStatus = 0;
            } else if (status == -1) {
                mRelationshipButton.setText("arkadaş ekle");
                mButtonStatus = 1;
            } else if (status == 0) {
                mRelationshipButton.setText("Kabul et");
                mButtonStatus = 3;
            }
        }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_user_activity_relationship_button: {
                switch (mButtonStatus) {
                    case 1: {
                        addRelationship(mUserId);
                        break;
                    }
                    case 3: {
                        acceptRelationship(mUserId);
                        break;
                    }
                    case 2: {
                        deleteRelationship(mUserId);
                        break;
                    }


                }
            }
        }
    }

    /*private void addRelationship(long mUserId) {
        UserRelationship userRelationship = UserRelationshipApiClient.getUserRelationshipClient(getApplicationContext());
        mAddFirend = userRelationship.addFriend(mUserId);
        mAddFirend.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            mRelationshipButton.setText("isteği iptal et");
                            mButtonStatus = 2;
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
                        mRelationshipButton.setText("isteği iptal et");
                        mButtonStatus = 2;
                    }
                });
    }*/
    private void addRelationship(long mUserId) {
        UserRelationship userRelationship = UserRelationshipApiClient.getUserRelationshipClient(getApplicationContext());
        mAddFirend = userRelationship.addFriend(mUserId);
        mAddFirend.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            mRelationshipButton.setText("isteği iptal et");
                            mButtonStatus = 2;
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

    private void acceptRelationship(long mUserId) {
        UserRelationship userRelationship = UserRelationshipApiClient.getUserRelationshipClient(getApplicationContext());
        mAcceptFirend = userRelationship.acceptFriend(mUserId);
        mAcceptFirend.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            mRelationshipButton.setText("Arkadaşlıktan cıkar");
                            mButtonStatus = 0;
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
    private void deleteRelationship(long mUserId) {
        UserRelationship userRelationship = UserRelationshipApiClient.getUserRelationshipClient(getApplicationContext());
        mDeleteFirend = userRelationship.deleteFriend(mUserId);
        mDeleteFirend.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> getUserInfoResponseModelResponse) {
                        if (getUserInfoResponseModelResponse.isSuccessful()) {
                            mRelationshipButton.setText("arkadaş ekle");
                            mButtonStatus = 1;
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
}

