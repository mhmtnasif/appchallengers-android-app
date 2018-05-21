package com.appchallengers.appchallengers.webservice.remote;


import com.appchallengers.appchallengers.helpers.util.Constants;
import com.appchallengers.appchallengers.helpers.util.GetUserToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserRelationshipApiClient {
    public static Context mContext;
    static int cacheSize = 10 * 1024 * 1024; // 10 MB
    static Cache cache = new Cache(Constants.contex.getCacheDir(), cacheSize);

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(new ResponseCacheInterceptor())
            .addInterceptor(new AuthInterceptor())
            .build();

    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.WEB_SERVİCE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static UserRelationship getUserRelationshipClient(Context context) {
        mContext=context;
        return getRetroClient().create(UserRelationship.class);
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
    private static class AuthInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request request=chain.request();
            Request.Builder builder=request.newBuilder().addHeader("token", new GetUserToken().getToken(mContext));
            return chain.proceed(builder.build());
        }
    }
}
