package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.GetUserInfoResponseModel;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.appchallengers.appchallengers.webservice.response.UserChallengeFeedListModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by MHMTNASIF on 19.03.2018.
 */

public interface GetProfileInfo {


    @GET("users/profile/info")
    Observable<Response<GetUserInfoResponseModel>> getProfileInfo();


    @GET("users/profile/get_challenges")
    Observable<Response<List<UserChallengeFeedListModel>>> getProfileChallenges();


    @GET("users/profile/get_accepted_challenges")
    Observable<Response<List<UserChallengeFeedListModel>>> getProfileAcceptedChallenges();


    @GET("users/profile/get_relationships")
    Observable<Response<List<UserBaseDataModel>>> getProfileFriends();
}
