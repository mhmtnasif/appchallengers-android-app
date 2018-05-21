package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.NotificationResponseModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface UserNotification {

    @GET("users/notifications/get_user_notification")
    Observable<Response<List<NotificationResponseModel>>> getNotificationList();
    @GET("users/notifications/has_notification")
    Observable<Response<Void>> hasNotification();
}
