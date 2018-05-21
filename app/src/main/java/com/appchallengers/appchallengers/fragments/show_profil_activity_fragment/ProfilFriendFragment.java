package com.appchallengers.appchallengers.fragments.show_profil_activity_fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowUserActivity;
import com.appchallengers.appchallengers.helpers.adapters.ShowLikesAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.GetProfileApiClient;
import com.appchallengers.appchallengers.webservice.remote.GetProfileInfo;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;



public class ProfilFriendFragment extends Fragment implements AdapterView.OnItemClickListener {


    private View mRootView;
    private ListView mUserFriendsListview;
    private List<UserBaseDataModel> mUserFriendsList;
    private ShowLikesAdapter mUserFriendsAdapter;
    private RotateLoading mFeedRotateLoading;
    private LinearLayout mLinearSadLayout;
    private CompositeDisposable mCompositeDisposable;
    Observable<Response<List<UserBaseDataModel>>> getProfileFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_profil_friend, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mUserFriendsList = new ArrayList<>();
        mUserFriendsListview = (ListView) mRootView.findViewById(R.id.profile_friends_fragment_listview);
        mCompositeDisposable = new CompositeDisposable();
        mLinearSadLayout=(LinearLayout) mRootView.findViewById(R.id.user_feed_sad_ll);
        mFeedRotateLoading = (RotateLoading) mRootView.findViewById(R.id.profile_friends_fragment_rotateloading);
        getProfileFriendList();
        mUserFriendsListview.setOnItemClickListener(this);
    }

    public void getProfileFriendList() {
        GetProfileInfo getProfileInfo = GetProfileApiClient.getProfileClientWithCache(getContext());
        getProfileFriends = getProfileInfo.getProfileFriends();
        getProfileFriends.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<UserBaseDataModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mFeedRotateLoading.start();
                    }

                    @Override
                    public void onNext(Response<List<UserBaseDataModel>> value) {
                        mUserFriendsList = value.body();
                        if (mUserFriendsList.size() != 0 && mUserFriendsList != null) {
                            mUserFriendsAdapter = new ShowLikesAdapter(getContext(), mUserFriendsList);
                            mUserFriendsListview.setAdapter(mUserFriendsAdapter);
                            mLinearSadLayout.setVisibility(View.GONE);
                        } else {
                                mLinearSadLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onComplete();
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
    public void onDestroy() {
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
        if (mFeedRotateLoading != null) {
            mFeedRotateLoading.stop();
            mFeedRotateLoading.setVisibility(View.GONE);
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        UserBaseDataModel userBaseDataModel = mUserFriendsList.get(i);
        Intent intent = new Intent(getActivity(), ShowUserActivity.class);
        intent.putExtra("user_id", userBaseDataModel.getId());
        startActivity(intent);
        getActivity().finish();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
