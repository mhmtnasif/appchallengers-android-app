package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.GetUserInfoResponseModel;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.appchallengers.appchallengers.webservice.response.UserChallengeFeedListModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by MHMTNASIF on 19.03.2018.
 */

public interface GetUserInfo {

    @FormUrlEncoded
    @POST("users/get_user/info")
    Observable<Response<GetUserInfoResponseModel>> getUserInfo(@Field("get_user_id") long get_user_id);

    @FormUrlEncoded
    @POST("users/get_user/get_challenges")
    Observable<Response<List<UserChallengeFeedListModel>>> getUserChallenges(@Field("get_user_id") long get_user_id);

    @FormUrlEncoded
    @POST("users/get_user/get_accepted_challenges")
    Observable<Response<List<UserChallengeFeedListModel>>> getUserAcceptedChallenges(@Field("get_user_id") long get_user_id);

    @FormUrlEncoded
    @POST("users/get_user/get_relationships")
    Observable<Response<List<UserBaseDataModel>>> getUserFriends(@Field("get_user_id") long get_user_id);
}
