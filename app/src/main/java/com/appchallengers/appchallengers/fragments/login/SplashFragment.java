package com.appchallengers.appchallengers.fragments.login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.database.CountriesTable;
import com.appchallengers.appchallengers.helpers.database.Database;
import com.appchallengers.appchallengers.helpers.setpages.SetLoginPages;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.InternetControl;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserAccountApiClient;
import com.appchallengers.appchallengers.webservice.remote.AppClient;
import com.appchallengers.appchallengers.webservice.response.CountryList;

import java.io.IOException;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;


public class SplashFragment extends Fragment {
    private TextView mSplashTextView;
    private SharedPreferences mSharedPreferences;
    private View mRootview;
    private boolean mStatus;
    private CompositeDisposable mCompositeDisposable;
    Observable<Response<List<CountryList>>> mListObservable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Utils.sharedPreferences = mSharedPreferences;
        mCompositeDisposable = new CompositeDisposable();
        mStatus = true;
        getCountryList();
        mRootview=inflater.inflate(R.layout.fragment_splash, container, false);
        mSplashTextView=(TextView) mRootview.findViewById(R.id.splash_fragment_textview);
        return mRootview;
    }


    private void getCountryList() {
        AppClient appClient = UserAccountApiClient.getAppClient();
        mListObservable = appClient.getCountryList();
        mListObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<CountryList>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<List<CountryList>> value) {
                        Database database = new Database();
                        CountriesTable countriesTable = new CountriesTable(database.open(getContext()));
                        for (CountryList countryList : value.body()) {
                            long status = countriesTable.create(countryList);
                            if (status == -1) {
                                mStatus = false;
                                break;
                            }
                        }
                        database.close();
                        if (mStatus) {
                            Utils.setSharedPreferencesBoolean("firstrun", false);
                            SetLoginPages.getInstance().constructor(getActivity(), 1);
                        }else{
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            InternetControl.getInstance().showSnack(mSplashTextView);
                            if (e instanceof java.net.ConnectException){
                                ErrorHandler.getInstance(getContext()).showInfo(300);
                            }
                        }else{
                            ErrorHandler.getInstance(getContext()).showEror("{code:1000}");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

}