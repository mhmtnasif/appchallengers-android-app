package com.appchallengers.appchallengers.helpers.util;

/**
 * Created by jir on 5.3.2018.
 */
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;

public class GetUserToken {

    public static String getToken(Context context){
        SharedPreferences mSharedPreferences= context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Utils.sharedPreferences=mSharedPreferences;
        return Utils.getPref("token");
    }
}
