package com.appchallengers.appchallengers.webservice.remote;


import com.appchallengers.appchallengers.webservice.response.UserChallengeFeedListModel;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import java.util.List;
public interface UserChallenges {

    @Multipart
    @POST("users/challenges/add_challenge")
    Call<UserChallengeFeedListModel> addChallenge(
            @Part("headLine") RequestBody headLine,
            @Part MultipartBody.Part video);

    @GET("users/challenges/get_user_challenge_feed")
    Observable<Response<List<UserChallengeFeedListModel>>> getUserChallengeFeed();

    @FormUrlEncoded
    @POST("users/challenges/add_challenge_notification")
    Observable<Response<Void>> addChallengeNotification(@Field("ChallengeId") long challengeId,
                                                        @Field("ChallengedList") String challengedList);



}
