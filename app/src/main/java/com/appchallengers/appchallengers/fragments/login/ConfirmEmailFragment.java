package com.appchallengers.appchallengers.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.appchallengers.appchallengers.MainActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;
import com.appchallengers.appchallengers.webservice.remote.UserAccount;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;




public class ConfirmEmailFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private CircularProgressButton mConfirmEmailValidate;
    private CircularProgressButton mConfirmEmailCodeSendAgain;
    private Observable<Response<Void>> mResponseObservableResent;
    private CompositeDisposable mCompositeDisposable;
    private Observable<Response<Void>> mResponseObservableCheckEmail;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_confirm_email, container, false);
        initalView(mRootView);
        return mRootView;

    }

    private void initalView(View view) {
        mCompositeDisposable = new CompositeDisposable();
        mConfirmEmailValidate = (CircularProgressButton) view.findViewById(R.id.confirm_email_fragment_email_validate_button);
        mConfirmEmailCodeSendAgain = (CircularProgressButton) view.findViewById(R.id.confirm_email_fragment_code_again_send_link_button);
        mConfirmEmailCodeSendAgain.setOnClickListener(this);
        mConfirmEmailValidate.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_email_fragment_email_validate_button: {
                mConfirmEmailValidate.startAnimation();
                mConfirmEmailValidate.setInitialCornerRadius(75);
                checkConfrimEmail();
                InternetControl.getInstance().showSnack(mConfirmEmailCodeSendAgain);
                break;
            }
            case R.id.confirm_email_fragment_code_again_send_link_button: {
                mConfirmEmailCodeSendAgain.startAnimation();
                mConfirmEmailCodeSendAgain.setInitialCornerRadius(75);
                userResendConfirmEmail();
                InternetControl.getInstance().showSnack(mConfirmEmailCodeSendAgain);
                break;
            }

        }

    }

    private void userResendConfirmEmail() {
        UserAccount userAccount = UserAccountApiClient.getUserAccountClient(getContext());
        mResponseObservableResent = userAccount.userResendConfirmEmail();
        mResponseObservableResent.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> value) {
                        if (value.isSuccessful()) {
                            ErrorHandler.getInstance(getContext()).showInfo(252);
                            ButtonActionPasif();
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

    private void checkConfrimEmail() {
        UserAccount userAccount = UserAccountApiClient.getUserAccountClient(getContext());
        mResponseObservableCheckEmail = userAccount.checkConfrimEmail();
        mResponseObservableCheckEmail.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<Void> value) {
                        if (value.isSuccessful()) {
                            Utils.setSharedPreferences("active", 1 + "");
                            ButtonActionPasif();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
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
                        ButtonActionPasif();
                    }
                });
    }



    private void ButtonActionPasif() {
        mConfirmEmailValidate.revertAnimation();
        mConfirmEmailCodeSendAgain.revertAnimation();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        }
    }

    @Override
    public void onDetach() {
        mConfirmEmailValidate.dispose();
        mConfirmEmailCodeSendAgain.dispose();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        if (mConfirmEmailValidate != null && mConfirmEmailValidate.isAnimating()) {
            mConfirmEmailValidate.revertAnimation();
            mConfirmEmailValidate.dispose();
        }
        if (mConfirmEmailCodeSendAgain != null && mConfirmEmailCodeSendAgain.isAnimating()) {
            mConfirmEmailCodeSendAgain.revertAnimation();
            mConfirmEmailCodeSendAgain.dispose();
        }
        super.onDestroy();
    }
}
