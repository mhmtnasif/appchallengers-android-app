package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by MHMTNASIF on 19.03.2018.
 */

public interface UserVote {

    @FormUrlEncoded
    @POST("users/votes/vote")
    Observable<Response<Void>> vote(@Field("challenge_detail_id") long challenge_detail_id);

    @FormUrlEncoded
    @POST("users/votes/get_vote_list")
    Observable<Response<List<UserBaseDataModel>>> getVoteList(@Field("challenge_detail_id") long challenge_detail_id);
}
