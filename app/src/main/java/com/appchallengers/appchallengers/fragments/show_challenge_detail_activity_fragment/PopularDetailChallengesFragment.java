package com.appchallengers.appchallengers.fragments.show_challenge_detail_activity_fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowProfilActivity;
import com.appchallengers.appchallengers.ShowUserActivity;
import com.appchallengers.appchallengers.helpers.adapters.DetailChallengeAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.Utils;
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

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;


public class PopularDetailChallengesFragment extends Fragment implements AdapterView.OnItemClickListener {


    private View mRootView;
    private long mPopularDetailChallengeId;
    private List<UserChallengeFeedListModel> mPopularDetailChallengeList;
    private DetailChallengeAdapter mDetailChallengeAdapter;
    private Container mContainer;
    private RotateLoading mFeedRotateLoading;
    private LinearLayoutManager mLinerlayoutmanager;
    private SharedPreferences mSharedPreferences;
    private CompositeDisposable mCompositeDisposable;
    Observable<Response<List<UserChallengeFeedListModel>>> getPopularDetailChallenges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_popular_detail_challenges,container,false);
        initialView(mRootView);
        return mRootView;
    }
    private void initialView(View mRootView) {
        mPopularDetailChallengeList = new ArrayList<>();
        mContainer = mRootView.findViewById(R.id.player_container);
        mLinerlayoutmanager = new LinearLayoutManager(getContext());
        mContainer.setLayoutManager(mLinerlayoutmanager);
        mCompositeDisposable = new CompositeDisposable();
        mSharedPreferences = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mFeedRotateLoading = (RotateLoading) mRootView.findViewById(R.id.fragment_popular_detail_Challenge_rotateloading);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("challenge_detail_id")) {
            mPopularDetailChallengeId = bundle.getLong("challenge_detail_id");
            getPopularDetailChallenge();
        } else {
            getActivity().finish();
        }
    }

    private void getPopularDetailChallenge() {
        final GetChallengeDetailInfo getChallengeDetailInfo = GetChallengeDetailInfoApiClient.getChallengeDetailInfoClientWithCache(getContext());
        getPopularDetailChallenges = getChallengeDetailInfo.getPopularChallengeDetail(mPopularDetailChallengeId);
        getPopularDetailChallenges.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<UserChallengeFeedListModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mFeedRotateLoading.start();
                    }
                    @Override
                    public void onNext(Response<List<UserChallengeFeedListModel>> detailChallengeListModelResponse) {
                        mPopularDetailChallengeList =detailChallengeListModelResponse.body();
                        mDetailChallengeAdapter = new DetailChallengeAdapter(getContext(), mPopularDetailChallengeList,getActivity());
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
                        mFeedRotateLoading.stop();
                        mFeedRotateLoading.setVisibility(View.GONE);
                    }
                });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Utils.sharedPreferences=mSharedPreferences;
        UserChallengeFeedListModel userChallengeFeedListModel = mPopularDetailChallengeList.get(i);
        try {
            if (Utils.getId(Utils.getPref("token"))==userChallengeFeedListModel.getChallenge_id()){
                Intent intent = new Intent(getActivity(), ShowProfilActivity.class);
                intent.putExtra("user_id", userChallengeFeedListModel.getChallenge_id());
                startActivity(intent);
                getActivity().finish();
            }
            else{
                Intent intent = new Intent(getActivity(), ShowUserActivity.class);
                intent.putExtra("user_id", userChallengeFeedListModel.getChallenge_id());
                startActivity(intent);
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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