package com.appchallengers.appchallengers.helpers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appchallengers.appchallengers.R;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;


public class Utils {
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    public static SharedPreferences sharedPreferences;

    public static void setSharedPreferences(String tag, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, value);
        editor.commit();
    }
    public static void setSharedPreferencesBoolean(String tag, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(tag, value);
        editor.commit();
    }

    public static String getPref(String tag) {
        String value = null;

        try {
            value = sharedPreferences.getString(tag, null).toString();
        } catch (Exception ex) {

        }
        return value;
    }public static boolean getPrefBoolean(String tag) {
        return sharedPreferences.getBoolean(tag,true);
    }



    public static boolean checkValidation(String[] components,
                                          LinearLayout linearLayout,
                                          Animation shakeAnimation,
                                          Context context,
                                          View view) {
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(components[0]);
        for (String component:components){
            if (component.equals("") || component.length() == 0) {
                linearLayout.startAnimation(shakeAnimation);
                new CustomToast().Show_Toast(context, view,
                        R.string.utils_empty_space);
                return false;
            }
        }
        if (!m.find()) {
            new CustomToast().Show_Toast(context, view,
                    R.string.utils_invaild_email);
            return false;
        }
        return true;

    }

    public static Integer getIdFromToken(String token) throws UnsupportedEncodingException {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey("secret".getBytes("UTF-8"))
                    .parseClaimsJws(token);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (Integer) claims.getBody().get("id");
    }

    public static long getId(String token) throws Exception {
        try {
            return Utils.getIdFromToken(token);
        } catch (MalformedJwtException exception) {
            throw new Exception();
        } catch (SignatureException exception) {
            throw new Exception();
        } catch (UnsupportedEncodingException e) {
            throw new Exception();
        }
    }
}
