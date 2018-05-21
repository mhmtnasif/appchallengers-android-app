package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.GetChallengeDetailResponseModel;
import com.appchallengers.appchallengers.webservice.response.UserChallengeFeedListModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by MHMTNASIF on 19.03.2018.
 */

public interface GetChallengeDetailInfo {

    @FormUrlEncoded
    @POST("users/challenges_detail/info")
    Observable<Response<GetChallengeDetailResponseModel>> getChallengeDetailInfo(@Field("challengeId") long get_challenge_id);

    @FormUrlEncoded
    @POST("users/challenges_detail/get_latest_challenges")
    Observable<Response<List<UserChallengeFeedListModel>>> getLatestChallengeDetail(@Field("challengeId") long get_challenge_id);

    @FormUrlEncoded
    @POST("users/challenges_detail/get_popular_challenges")
    Observable<Response<List<UserChallengeFeedListModel>>> getPopularChallengeDetail(@Field("challengeId") long get_challenge_id);

    @Multipart
    @POST("users/challenges_detail/add_challenge_detail")
    Call<UserChallengeFeedListModel> addChallengeDetail(
            @Part("challengeId") long challengeId,
            @Part MultipartBody.Part video);

}
