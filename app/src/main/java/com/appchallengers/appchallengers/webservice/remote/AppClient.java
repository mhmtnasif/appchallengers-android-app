package com.appchallengers.appchallengers.webservice.remote;

import com.appchallengers.appchallengers.webservice.response.CountryList;
import com.appchallengers.appchallengers.webservice.response.TrendsDataModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by MHMTNASIF on 22.02.2018.
 */

public interface AppClient {

    @GET("application/get_country_list")
    Observable<Response<List<CountryList>>> getCountryList();

    @GET("application/get_trends_list")
    Observable<Response<List<TrendsDataModel>>> getTrendsList();
}
