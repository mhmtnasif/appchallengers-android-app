package com.appchallengers.appchallengers.webservice.remote;

import android.content.Context;

import com.appchallengers.appchallengers.helpers.util.Constants;
import com.appchallengers.appchallengers.helpers.util.GetUserToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MHMTNASIF on 19.03.2018.
 */

public class UserVoteApiClient {

    public static Context mContext;
    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();

    static OkHttpClient okHttpClientResponseCache = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .addNetworkInterceptor(new ResponseCacheInterceptor())
            .build();

    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.WEB_SERVİCE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static Retrofit getRetroClientWithCache() {
        return new Retrofit.Builder()
                .client(okHttpClientResponseCache)
                .baseUrl(Constants.WEB_SERVİCE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static UserVote getVoteClient(Context context) {
        mContext = context;
        return getRetroClient().create(UserVote.class);
    }

    public static UserVote getVoteClientWithCache(Context context) {
        mContext = context;
        return getRetroClientWithCache().create(UserVote.class);
    }

    private static class AuthInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder().addHeader("token", new GetUserToken().getToken(mContext));
            return chain.proceed(builder.build());
        }
    }

    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build();
        }
    }
}
