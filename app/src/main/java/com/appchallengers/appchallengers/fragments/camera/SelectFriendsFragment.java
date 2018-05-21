package com.appchallengers.appchallengers.fragments.camera;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appchallengers.appchallengers.MainActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.adapters.FriendsListAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.webservice.remote.UserRelationship;
import com.appchallengers.appchallengers.webservice.remote.UserRelationshipApiClient;
import com.appchallengers.appchallengers.webservice.response.FriendsList;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class SelectFriendsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View mRootView;
    private List<FriendsList> mFriendList;
    private String mPath;
    private FriendsListAdapter mFriendsListAdapter;
    private ListView mSelectFriendsListview;
    private EditText mSearchEdittext;
    private CircularProgressButton mChallengeButton;
    private TextView mGetSelectedItems;
    private LinearLayout mLinearSadLayout;
    private TextView mSelectAll;
    private boolean isShowTheSelected;
    private boolean isSelectAll;
    private RotateLoading mRotateLoading;
    private Observable<Response<List<FriendsList>>> mResponseObservable;
    private CompositeDisposable mCompositeDisposable;
    long challengeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_select_friends, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        Bundle bundle = getArguments();
        if (!bundle.isEmpty()) {
            mPath = bundle.getString("path");
            challengeId = bundle.getLong("challengeId");

        } else {
            errorHandler();
        }
        mCompositeDisposable = new CompositeDisposable();
        isShowTheSelected = false;
        isSelectAll = false;
        mRotateLoading = (RotateLoading) mRootView.findViewById(R.id.select_friends_fragment_rotateloading);
        getFriendList();
        mSelectFriendsListview = (ListView) mRootView.findViewById(R.id.select_Friends_fragment_listview);
        mSearchEdittext = (EditText) mRootView.findViewById(R.id.select_friends_fragment_edittext);
        mGetSelectedItems = (TextView) mRootView.findViewById(R.id.select_friends_fragment_show_all_edittext);
        mSelectAll = (TextView) mRootView.findViewById(R.id.select_friends_fragment_select_all_edittext);
        mLinearSadLayout=(LinearLayout) mRootView.findViewById(R.id.user_feed_sad_ll);
        mChallengeButton = (CircularProgressButton) mRootView.findViewById(R.id.select_friends_fragment_challenge_button);
        if (challengeId!=-999){
            mChallengeButton.setText("challenge");
        }
        mSelectFriendsListview.setOnItemClickListener(this);
        mGetSelectedItems.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mChallengeButton.setOnClickListener(this);
        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isShowTheSelected) {
                    mFriendsListAdapter.filterTheSelected(charSequence.toString());
                    mSelectFriendsListview.invalidate();
                } else {
                    mFriendsListAdapter.filter(charSequence.toString());
                    mSelectFriendsListview.invalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getFriendList() {
        UserRelationship userRelationship = UserRelationshipApiClient.getUserRelationshipClient(getContext());
        mResponseObservable = userRelationship.getFriendList();
        mResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FriendsList>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mRotateLoading.start();
                    }
                    @Override
                    public void onNext(Response<List<FriendsList>> value) {
                        if (value.isSuccessful()) {
                            mFriendList = value.body();
                            if (mFriendList.size() != 0 && mFriendList != null) {
                                mFriendsListAdapter = new FriendsListAdapter(getContext(), mFriendList);
                                mSelectFriendsListview.setAdapter(mFriendsListAdapter);
                                mLinearSadLayout.setVisibility(View.GONE);
                            } else {
                                mLinearSadLayout.setVisibility(View.VISIBLE);
                            }
                        }else{
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        mRotateLoading.stop();
                        if (e instanceof IOException) {
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
                    }
                });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.friend_list_checkbox);
            checkBox.setChecked(!checkBox.isChecked());
        }
        if (isShowTheSelected) {
            if (!mFriendsListAdapter.getSelectedItems()) {
                mGetSelectedItems.setText(R.string.fragment_select_friends_show_the_selected);
                isShowTheSelected = false;
            }
            mSelectFriendsListview.invalidate();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_friends_fragment_show_all_edittext: {
                if (isShowTheSelected) {
                    isShowTheSelected = false;
                    mGetSelectedItems.setText(R.string.fragment_select_friends_show_the_selected);
                    mFriendsListAdapter.getAll();
                    mSelectFriendsListview.invalidate();
                } else {
                    isShowTheSelected = true;
                    mGetSelectedItems.setText(R.string.fragment_select_friends_show_all);
                    mFriendsListAdapter.getSelectedItems();
                    mSelectFriendsListview.invalidate();
                }
                break;
            }
            case R.id.select_friends_fragment_select_all_edittext: {
                if (isSelectAll) {
                    isSelectAll = false;
                    isShowTheSelected = false;
                    mGetSelectedItems.setText(R.string.fragment_select_friends_show_the_selected);
                    mSelectAll.setText(R.string.fragment_select_friends_select_all);
                    mFriendsListAdapter.unSelectAll();
                    mSelectFriendsListview.invalidate();
                } else {
                    isSelectAll = true;
                    mSelectAll.setText(R.string.fragment_select_friends_unselect_all);
                    mFriendsListAdapter.selectAll();
                    mSelectFriendsListview.invalidate();
                }
                break;
            }
            case R.id.select_friends_fragment_challenge_button: {
                if (challengeId==-999){
                    showBottomSheetDialogFragment();
                }else{
                    List<FriendsList> selectList = mFriendsListAdapter.getSelectedItemsList();
                    ArrayList<FriendsList> selectListFriend = new ArrayList<>(selectList);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("status", true);
                    intent.putExtra("path", mPath);
                    intent.putExtra("challengeId", challengeId);
                    intent.putParcelableArrayListExtra("selectfriendlist", selectListFriend);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        }
    }

    public void showBottomSheetDialogFragment() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
        final EditText mEditext = (EditText) view.findViewById(R.id.bottom_sheet_dialog_fragment_header_edittext);
        view.findViewById(R.id.bottom_sheet_dialog_fragment_challenge_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetControl.getInstance().InternetControl()) {
                    ErrorHandler.getInstance(getContext()).showInfo(151);
                } else if (TextUtils.isEmpty(mEditext.getText())) {
                    ErrorHandler.getInstance(getContext()).showInfo(150);
                } else if (mFriendsListAdapter.getSelectedItemsList().size() == 0 || mFriendsListAdapter.getSelectedItemsList() == null) {
                    ErrorHandler.getInstance(getContext()).showInfo(152);
                } else {
                    mBottomSheetDialog.dismiss();
                    List<FriendsList> selectList = mFriendsListAdapter.getSelectedItemsList();
                    ArrayList<FriendsList> selectListFriend = new ArrayList<>(selectList);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("status", true);
                    intent.putExtra("headLine", mEditext.getText().toString());
                    intent.putExtra("path", mPath);
                    intent.putExtra("challengeId", challengeId);
                    intent.putParcelableArrayListExtra("selectfriendlist", selectListFriend);
                    startActivity(intent);
                    getActivity().finish();

                }
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
            mRotateLoading.setVisibility(View.GONE);
        }
        super.onDestroy();
    }


    private void errorHandler() {
        ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
        getActivity().finish();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.DOWN, enter, 500);
        }
    }
}
