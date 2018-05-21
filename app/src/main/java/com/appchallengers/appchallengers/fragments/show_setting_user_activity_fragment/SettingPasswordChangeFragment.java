package com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowProfilActivity;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.UserAccount;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class SettingPasswordChangeFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mAgainNewdPassword;
    private CircularProgressButton mPasswordUpDateButton;
    private CompositeDisposable mCompositeDisposable;
    private Animation mShakeAnimation;
    private LinearLayout mLinearLayout;
    private Observable<Response<Void>> mProfilePasswordSetting;

    public SettingPasswordChangeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_setting_password_change, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mCompositeDisposable = new CompositeDisposable();
        mOldPassword = (EditText) mRootView.findViewById(R.id.fragment_setting_password_old_edittext);
        mNewPassword = (EditText) mRootView.findViewById(R.id.fragment_setting_password_new_edittext);
        mAgainNewdPassword = (EditText) mRootView.findViewById(R.id.fragment_setting_password_new_again_edittext);
        mLinearLayout = (LinearLayout) mRootView.findViewById(R.id.fragment_settin_password_ll);
        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mPasswordUpDateButton = (CircularProgressButton) mRootView.findViewById(R.id.setting_fragment_password_up_date_button);
        mPasswordUpDateButton.setOnClickListener(this);
    }

    private void upDateAccountPasswordChange() {
        UserAccount userAccount = UserAccountApiClient.getUserAccountClient(getContext());
        String newpassword = mNewPassword.getText().toString();
        String oldpassword = mOldPassword.getText().toString();
        mProfilePasswordSetting = userAccount.changePassword(oldpassword, newpassword);
        mProfilePasswordSetting.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ButtonActionActive();
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> value) {
                        if (value.isSuccessful()) {
                            ButtonActionPasif();
                            startActivity(new Intent(getContext(), ShowProfilActivity.class));
                            getActivity().finish();
                        } else {
                            if (value.code() == 400) {
                                if (value.errorBody() != null) {
                                    try {
                                        ErrorHandler.getInstance(getContext()).showEror(value.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                            }
                            ButtonActionPasif();
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
                        ButtonActionPasif();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ButtonActionActive() {
        mPasswordUpDateButton.startAnimation();
        mPasswordUpDateButton.setInitialCornerRadius(75);
    }

    private void ButtonActionPasif() {
        mPasswordUpDateButton.revertAnimation();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_fragment_password_up_date_button: {
                if (mNewPassword.getText().toString().equals("")) {
                    mLinearLayout.startAnimation(mShakeAnimation);
                    ErrorHandler.getInstance(getContext()).showEror("{code:358}");
                }
                else{
                    if ((mNewPassword.getText().toString()).equals(mAgainNewdPassword.getText().toString())) {
                        upDateAccountPasswordChange();
                    } else {
                        ErrorHandler.getInstance(getContext()).showEror("{code:356}");
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
