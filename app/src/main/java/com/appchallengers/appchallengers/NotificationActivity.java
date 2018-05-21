package com.appchallengers.appchallengers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appchallengers.appchallengers.helpers.adapters.NotificationAdapter;
import com.appchallengers.appchallengers.helpers.util.ErrorHandler;
import com.appchallengers.appchallengers.webservice.remote.UserNotification;
import com.appchallengers.appchallengers.webservice.remote.UserNotificationApiClient;
import com.appchallengers.appchallengers.webservice.response.NotificationResponseModel;
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

public class NotificationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<NotificationResponseModel> mNotificationList;
    private NotificationAdapter mNotificationAdapter;
    private ListView mNotificationListView;
    private RotateLoading mRotateLoading;
    private Observable<Response<List<NotificationResponseModel>>> mNotificationListObservable;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initialView();
    }

    private void initialView() {
        mNotificationListView = (ListView) findViewById(R.id.natification_list);
        mRotateLoading = (RotateLoading) findViewById(R.id.notification_rotateloading);
        mCompositeDisposable = new CompositeDisposable();
        mNotificationListView.setOnItemClickListener(this);
        getNotificationList();
    }

    private void getNotificationList() {
        UserNotification userNotification = UserNotificationApiClient.getUserChallengesClient(getApplicationContext());
        mNotificationListObservable = userNotification.getNotificationList();
        mNotificationListObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<NotificationResponseModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        mRotateLoading.start();
                    }

                    @Override
                    public void onNext(Response<List<NotificationResponseModel>> value) {
                        if (value.isSuccessful()) {
                            mNotificationList = value.body();
                            mNotificationAdapter = new NotificationAdapter(getApplicationContext(), mNotificationList);
                            mNotificationListView.setAdapter(mNotificationAdapter);
                        } else {
                            ErrorHandler.getInstance(getApplicationContext()).showEror("{code:1000}");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRotateLoading.stop();
                        if (e instanceof IOException) {
                            if (e instanceof java.net.ConnectException) {
                                ErrorHandler.getInstance(getApplicationContext()).showInfo(300);
                            }
                        } else {
                            ErrorHandler.getInstance(getApplicationContext()).showEror("{code:1000}");
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
        NotificationResponseModel notificationResponseModel = mNotificationList.get(i);
        Intent intent = new Intent(NotificationActivity.this, ShowUserActivity.class);
        intent.putExtra("user_id", notificationResponseModel.getActionId());
        startActivity(intent);
        finish();
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
}
