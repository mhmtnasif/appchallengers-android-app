package com.appchallengers.appchallengers.webservice.remote;


import com.appchallengers.appchallengers.webservice.request.SignUpRequestModel;
import com.appchallengers.appchallengers.webservice.request.UsersLoginRequestModel;
import com.appchallengers.appchallengers.webservice.response.SearchChallengesResponseModel;
import com.appchallengers.appchallengers.webservice.response.SearchResponseModel;
import com.appchallengers.appchallengers.webservice.response.UserBaseDataModel;
import com.appchallengers.appchallengers.webservice.response.UserPreferencesData;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;


public interface UserSearch {
    @FormUrlEncoded
    @POST("users/search/search")
    Observable<Response<SearchResponseModel>> serachUserFeed(@Field("param") String param);
}
