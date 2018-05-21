package com.appchallengers.appchallengers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import 	android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.appchallengers.appchallengers.helpers.adapters.MainFeedSearchAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.appchallengers.appchallengers.webservice.remote.UserSearch;
import com.appchallengers.appchallengers.webservice.remote.UserSearchApiClient;
import com.appchallengers.appchallengers.webservice.response.SearchResponseModel;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;

public class ShowSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView mSearchEditText;
    private ProgressBar mLoadingIndicator;
    private Observable<Response<SearchResponseModel>> responseObservable;
    private CompositeDisposable mCompositeDisposable;
    private SharedPreferences mSharedPreferences;
    private List<UserBaseDataModel> mResponsUserBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search);
        initialView();
    }
    private void initialView(){
        mCompositeDisposable = new CompositeDisposable();
        mResponsUserBase = new ArrayList<>();
        mSearchEditText = (AutoCompleteTextView) findViewById(R.id.activity_search_edittext);
        mLoadingIndicator=(ProgressBar) findViewById(R.id.activity_search_progres);
        mSharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    getUserChallengeFeed(charSequence.toString());
                    mLoadingIndicator.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                mSearchEditText.setThreshold(2);
                mSearchEditText.setAdapter(new MainFeedSearchAdapter(getApplicationContext(), mResponsUserBase));
                mLoadingIndicator.setVisibility(View.GONE);
            }
        });
        mSearchEditText.setOnItemClickListener(this);
    }
    public void getUserChallengeFeed(String param) {
        UserSearch userSearch = UserSearchApiClient.getUserSearchClient(getApplicationContext());
        responseObservable = userSearch.serachUserFeed(param);
        responseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SearchResponseModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<SearchResponseModel> userSearchListModelResponse) {
                        mResponsUserBase = userSearchListModelResponse.body().getUserList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getApplicationContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getApplicationContext()).showEror("{code:1000}");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                });

    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Utils.sharedPreferences=mSharedPreferences;
        UserBaseDataModel userBaseDataModel = mResponsUserBase.get(i);
        mSearchEditText.setText("");
        try {
            if (Utils.getId(Utils.getPref("token"))==userBaseDataModel.getId()){
                Intent intent = new Intent(this, ShowProfilActivity.class);
                intent.putExtra("user_id", userBaseDataModel.getId());
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(this, ShowUserActivity.class);
                intent.putExtra("user_id", userBaseDataModel.getId());
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
