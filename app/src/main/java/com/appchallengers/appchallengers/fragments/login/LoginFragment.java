package com.appchallengers.appchallengers.fragments.login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.WebViewActivity;
import com.appchallengers.appchallengers.helpers.setpages.SetLoginPages;
import com.appchallengers.appchallengers.helpers.util.Constants;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserAccount;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;
import com.appchallengers.appchallengers.webservice.request.UsersLoginRequestModel;
import com.appchallengers.appchallengers.webservice.response.UserPreferencesData;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.io.IOException;
import android.content.Intent;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import io.reactivex.Observable;
import io.reactivex.Observer;
import com.appchallengers.appchallengers.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private EditText mLoginFragmentUserEmail;
    private EditText mLoginFragmentPassword;
    private CircularProgressButton mLoginFragmentLogin;
    private TextView mLoginFragmentForgotPassword;
    private LinearLayout mLinearLayout;
    private Animation mShakeAnimation;
    private SharedPreferences mSharedPreferences;
    private ImageView mLoginBackArrow;
    private CompositeDisposable mCompositeDisposable;
    private Observable<Response<UserPreferencesData>> mResponseObservable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View rootView) {
        mCompositeDisposable = new CompositeDisposable();
        mLoginFragmentUserEmail = (EditText) rootView.findViewById(R.id.login_fragment_email_edittext);
        mLoginFragmentPassword = (EditText) rootView.findViewById(R.id.login_fragment_password_edittext);
        mLoginFragmentLogin = (CircularProgressButton) rootView.findViewById(R.id.login_fragment_login_button);
        mLoginBackArrow = (ImageView) mRootView.findViewById(R.id.login_fragment_back_arrow_imageview);
        mLoginFragmentForgotPassword = (TextView) rootView.findViewById(R.id.login_fragment_forgot_password_textview);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.login_fragment_login_image_ll);
        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mSharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mLoginFragmentLogin.setOnClickListener(this);
        mLoginFragmentForgotPassword.setOnClickListener(this);
        mLoginBackArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_fragment_forgot_password_textview: {
                Intent intent=new  Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("web",Constants.URL_FORGOT_PASSWORD);
                getActivity().startActivity(intent);
                break;
            }
            case R.id.login_fragment_login_button: {
                if (checkValidation()) {
                    ButtonActionActive();
                    usersLogin();
                    InternetControl.getInstance().showSnack(mLoginFragmentForgotPassword);
                }
                break;
            }
            case R.id.login_fragment_back_arrow_imageview: {
                SetLoginPages.getInstance().constructor(getActivity(), 1);
            }

        }
    }

    private void usersLogin() {
        UserAccount userAccount = UserAccountApiClient.getUserAccount();
        String email = mLoginFragmentUserEmail.getText().toString();
        String password = mLoginFragmentPassword.getText().toString();
        mResponseObservable = userAccount.usersLogin(new UsersLoginRequestModel(email, password));
        mResponseObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserPreferencesData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<UserPreferencesData> value) {
                        if (value.isSuccessful()) {
                            Utils.sharedPreferences = mSharedPreferences;
                            Utils.setSharedPreferences("token", value.body().getToken());
                            Utils.setSharedPreferences("fullName", value.body().getFullName());
                            Utils.setSharedPreferences("imageUrl", value.body().getImageUrl());
                            Utils.setSharedPreferences("email", value.body().getEmail());
                            Utils.setSharedPreferences("active", value.body().getActive() + "");
                            Utils.setSharedPreferences("country", value.body().getCountry());

                            ButtonActionPasif();
                            if (value.body().getActive() == 0) {
                                SetLoginPages.getInstance().constructor(getActivity(), 4);
                            } else if (value.body().getActive() == 1) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        } else{
                            if (value.code()==400){
                                if (value.errorBody()!=null){
                                    try {
                                        ErrorHandler.getInstance(getContext()).showEror(value.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                            }
                            ButtonActionPasif();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            if (e instanceof java.net.ConnectException){
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        }else{
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                        ButtonActionPasif();
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }


    private boolean checkValidation() {
        String email = mLoginFragmentUserEmail.getText().toString();
        String password = mLoginFragmentPassword.getText().toString();
        return Utils.checkValidation(new String[]{email, password}, mLinearLayout, mShakeAnimation, getActivity(), mRootView);
    }

    private void ButtonActionActive() {
        mLoginFragmentLogin.startAnimation();
        mLoginFragmentLogin.setInitialCornerRadius(75);
    }

    private void ButtonActionPasif() {
       mLoginFragmentLogin.revertAnimation();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
        }
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        if (mLoginFragmentLogin != null && mLoginFragmentLogin.isAnimating()) {
            mLoginFragmentLogin.revertAnimation();
            mLoginFragmentLogin.dispose();
        }
        super.onDestroy();
    }


}
