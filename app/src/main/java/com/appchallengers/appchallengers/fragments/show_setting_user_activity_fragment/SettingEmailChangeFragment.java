package com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment;


import android.content.Intent;
import android.content.SharedPreferences;
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
import com.appchallengers.appchallengers.helpers.util.Utils;
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

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;


public class SettingEmailChangeFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private EditText mOldEmail;
    private EditText mNewEmail;
    private EditText mAgainNewdEmail;
    private CircularProgressButton mEmailUpDateButton;
    private CompositeDisposable mCompositeDisposable;
    private LinearLayout mLinearLayout;
    private SharedPreferences mSharedPreferences;
    private Animation mShakeAnimation;
    private Observable<Response<Void>> mProfileEmailSetting;

    public SettingEmailChangeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_setting_email_change, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mCompositeDisposable = new CompositeDisposable();
        mOldEmail = (EditText) mRootView.findViewById(R.id.fragment_setting_email_old_edittext);
        mNewEmail = (EditText) mRootView.findViewById(R.id.fragment_setting_email_new_edittext);
        mAgainNewdEmail = (EditText) mRootView.findViewById(R.id.fragment_setting_email_new_again_edittext);
        mLinearLayout = (LinearLayout) mRootView.findViewById(R.id.setting_fragment_email_ll);
        mEmailUpDateButton = (CircularProgressButton) mRootView.findViewById(R.id.setting_fragment_email_up_date_button);
        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mSharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mEmailUpDateButton.setOnClickListener(this);
    }

    private void upDateAccountEmailChange() {
        UserAccount userAccount = UserAccountApiClient.getUserAccountClient(getContext());
        String newemail = mNewEmail.getText().toString();
        String oldemail = mOldEmail.getText().toString();
        mProfileEmailSetting = userAccount.changeEmail(oldemail, newemail);
        mProfileEmailSetting.subscribeOn(Schedulers.newThread())
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
                            Utils.sharedPreferences = mSharedPreferences;
                            Utils.setSharedPreferences("email", mNewEmail.getText().toString());
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
        mEmailUpDateButton.startAnimation();
        mEmailUpDateButton.setInitialCornerRadius(75);
    }

    private void ButtonActionPasif() {
        mEmailUpDateButton.revertAnimation();
    }

    private boolean checkValidation() {
        String email = mNewEmail.getText().toString();
        return Utils.checkValidation(new String[]{email}, mLinearLayout, mShakeAnimation, getActivity(), mRootView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_fragment_email_up_date_button: {
                if ((mNewEmail.getText().toString()).equals(mAgainNewdEmail.getText().toString())) {
                    if (checkValidation()) {
                        upDateAccountEmailChange();
                    }
                } else {
                    ErrorHandler.getInstance(getContext()).showEror("{code:357}");
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
