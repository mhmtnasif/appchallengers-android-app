package com.appchallengers.appchallengers.fragments.show_user_activity_fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.adapters.UserInfoChallengeFeedAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.GetUserInfo;
import com.appchallengers.appchallengers.webservice.remote.GetUserInfoApiClient;
import com.appchallengers.appchallengers.webservice.response.UserChallengeFeedListModel;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import im.ene.toro.widget.Container;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class User_ChallengeFragment extends Fragment {
    private View mRootView;
    private long mUserId;
    private List<UserChallengeFeedListModel> mUserChallengeInfoFeedList;
    private UserInfoChallengeFeedAdapter mUserInfoChallengeFeedAdapter;
    private Container mContainer;
    private RotateLoading mFeedRotateLoading;
    private LinearLayout mLinearSadLayout;
    private LinearLayoutManager mLinerlayoutmanager;
    private CompositeDisposable mCompositeDisposable;
    Observable<Response<List<UserChallengeFeedListModel>>> getUserChallenges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_show_user_challenge, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mUserChallengeInfoFeedList = new ArrayList<>();
        mContainer = mRootView.findViewById(R.id.player_container);
        mLinerlayoutmanager = new LinearLayoutManager(getContext());
        mContainer.setLayoutManager(mLinerlayoutmanager);
        mCompositeDisposable = new CompositeDisposable();
        mFeedRotateLoading = (RotateLoading) mRootView.findViewById(R.id.activity_show_user_rotateloading);
        mLinearSadLayout=(LinearLayout) mRootView.findViewById(R.id.user_feed_sad_ll);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("user_id")) {
            mUserId = bundle.getLong("user_id");
            getUserInfoChallengeFeed();
        } else {
            getActivity().finish();
        }
    }

    private void getUserInfoChallengeFeed() {
        GetUserInfo getUserInfo = GetUserInfoApiClient.getUserInfoClientWithCache(getContext());
        getUserChallenges = getUserInfo.getUserChallenges(mUserId);
        getUserChallenges.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<UserChallengeFeedListModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mFeedRotateLoading.start();
                    }

                    @Override
                    public void onNext(Response<List<UserChallengeFeedListModel>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            mUserChallengeInfoFeedList.addAll(listResponse.body());
                            mUserInfoChallengeFeedAdapter = new UserInfoChallengeFeedAdapter(getContext(), listResponse.body(), getActivity());
                            mContainer.setAdapter(mUserInfoChallengeFeedAdapter);
                            if(mUserChallengeInfoFeedList.size()==0){
                                mLinearSadLayout.setVisibility(View.VISIBLE);
                            }
                            else mLinearSadLayout.setVisibility(View.GONE);
                        } else {
                            if (listResponse.code() == 400) {
                                if (listResponse.errorBody() != null) {
                                    try {
                                        ErrorHandler.getInstance(getContext()).showEror(listResponse.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                        mFeedRotateLoading.stop();
                        mFeedRotateLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mFeedRotateLoading.stop();
                        mFeedRotateLoading.setVisibility(View.GONE);
                    }
                });
    }


    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
        if (mFeedRotateLoading != null) {
            mFeedRotateLoading.stop();
            mFeedRotateLoading.setVisibility(View.GONE);
        }
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}

