package com.appchallengers.appchallengers.fragments.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.adapters.TrendsRecycleViewAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.AppClient;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;
import com.appchallengers.appchallengers.webservice.response.TrendsDataModel;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class TrendsFeedFragment extends Fragment {

    private View mRootView;
    private RecyclerView mTrendsRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private TrendsRecycleViewAdapter mTrendsRecycleViewAdapter;
    private List<TrendsDataModel> mTrendsDataModelList;
    private RotateLoading mRotateLoading;
    private CompositeDisposable mCompositeDisposable;
    Observable<Response<List<TrendsDataModel>>> mTrendListObservable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_trends_feed, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mTrendsRecycleView = (RecyclerView) mRootView.findViewById(R.id.trends_recycleview);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mTrendsRecycleView.setLayoutManager(mLinearLayoutManager);
        mCompositeDisposable = new CompositeDisposable();
        mRotateLoading = (RotateLoading) mRootView.findViewById(R.id.trends_fragment_rotateloading);
        getTrendsList();
    }

    private void getTrendsList() {
        AppClient appClient = UserAccountApiClient.getAppClient();
        mTrendListObservable = appClient.getTrendsList();
        mTrendListObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<TrendsDataModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRotateLoading.start();
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<List<TrendsDataModel>> value) {
                        if (value.isSuccessful()) {
                            mTrendsDataModelList = value.body();
                            mTrendsRecycleViewAdapter = new TrendsRecycleViewAdapter(mTrendsDataModelList, getActivity());
                            mTrendsRecycleView.setAdapter(mTrendsRecycleViewAdapter);
                        } else {
                            ErrorHandler.getInstance(getContext()).showEror("1000");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                           // InternetControl.getInstance().showSnack(mSplashTextView);
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                        mRotateLoading.stop();
                    }

                    @Override
                    public void onComplete() {
                        mRotateLoading.stop();
                        mRotateLoading.setVisibility(View.GONE);
                        mTrendsRecycleView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        if (mRotateLoading != null && mRotateLoading.isStart()) {
            mRotateLoading.stop();
        }
        super.onDestroy();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        }
    }

}
