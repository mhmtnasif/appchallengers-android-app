package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.FriendsList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by MHMTNASIF on 2.03.2018.
 */

public interface UserRelationship {

    @GET("users/relationship/get_friend_list")
    Observable<Response<List<FriendsList>>> getFriendList();

    @FormUrlEncoded
    @POST("users/relationship/addfriend")
    Observable<Response<Void>> addFriend(@Field("request_id") long request_id);

    @FormUrlEncoded
    @PUT("users/relationship/accept")
    Observable<Response<Void>> acceptFriend(@Field("request_id") long request_id);

    @FormUrlEncoded
    @HTTP(hasBody = true,method = "DELETE",path = "users/relationship/delete")
    Observable<Response<Void>> deleteFriend(@Field("request_id") long request_id);
}
