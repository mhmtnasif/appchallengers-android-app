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

public class UserNotificationApiClient {

    public static Context mContext;
    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new UserNotificationApiClient.AuthInterceptor())
            .addNetworkInterceptor(new UserNotificationApiClient.ResponseCacheInterceptor())
            .build();

    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(Constants.WEB_SERVİCE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static UserNotification getUserChallengesClient(Context context) {
        mContext = context;
        return getRetroClient().create(UserNotification.class);
    }

    private static class AuthInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
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
