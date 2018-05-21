package com.appchallengers.appchallengers.fragments.show_challenge_detail_activity_fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.adapters.DetailChallengeAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.GetChallengeDetailInfo;
import com.appchallengers.appchallengers.webservice.remote.GetChallengeDetailInfoApiClient;
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

public class LatestDetailChallengeFragment extends Fragment {
    private View mRootView;
    private Container mContainer;
    private LinearLayoutManager mLinerlayoutmanager;
    private RotateLoading mRotateLoading;
    private Bundle mBundle;
    private Long mDetailChallengeId;
    private CompositeDisposable mCompositeDisposable;
    private DetailChallengeAdapter mDetailChallengeAdapter;
    private List<UserChallengeFeedListModel> mLatestChallengeDetailList;
    Observable<Response<List<UserChallengeFeedListModel>>> getLatestDetailChallenges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detail_challenge, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mLatestChallengeDetailList = new ArrayList<>();
        mContainer = mRootView.findViewById(R.id.player_container);
        mLinerlayoutmanager = new LinearLayoutManager(getContext());
        mContainer.setLayoutManager(mLinerlayoutmanager);
        mCompositeDisposable = new CompositeDisposable();
        mRotateLoading = (RotateLoading) mRootView.findViewById(R.id.fragment_detail_Challenge_rotateloading);
         mBundle = getArguments();
        if (mBundle != null && mBundle.containsKey("challenge_detail_id")) {
            mDetailChallengeId = mBundle.getLong("challenge_detail_id");
            getDetailChallenge();
        } else {
            getActivity().finish();
        }
    }

    private void getDetailChallenge() {
        final GetChallengeDetailInfo getChallengeDetailInfo = GetChallengeDetailInfoApiClient.getChallengeDetailInfoClientWithCache(getContext());
        getLatestDetailChallenges = getChallengeDetailInfo.getLatestChallengeDetail(mDetailChallengeId);
        getLatestDetailChallenges.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<UserChallengeFeedListModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mRotateLoading.start();
                    }

                    @Override
                    public void onNext(Response<List<UserChallengeFeedListModel>> detailChallengeListModelResponse) {
                            mLatestChallengeDetailList =detailChallengeListModelResponse.body();
                            mDetailChallengeAdapter = new DetailChallengeAdapter(getContext(), mLatestChallengeDetailList,getActivity());
                            mContainer.setAdapter(mDetailChallengeAdapter);
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
                    }

                    @Override
                    public void onComplete() {
                        mRotateLoading.stop();
                        mRotateLoading.setVisibility(View.GONE);
                    }
                });
    }
    public void fabState(int state){

    }


    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
        if (mRotateLoading != null) {
            mRotateLoading.stop();
            mRotateLoading.setVisibility(View.GONE);
        }
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}

