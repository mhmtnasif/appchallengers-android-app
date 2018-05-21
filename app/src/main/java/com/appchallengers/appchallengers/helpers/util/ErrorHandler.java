package com.appchallengers.appchallengers.helpers.util;

import android.content.Context;

import com.appchallengers.appchallengers.R;
import com.google.gson.JsonParser;

/**
 * Created by jir on 8.3.2018.
 */

public class ErrorHandler {

    public static Context mContext;
    public static ErrorHandler errorHandler = null;
    private static Object object = new Object();

    private ErrorHandler() {
    }

    public static ErrorHandler getInstance(Context context) {
        mContext = context;
        if (errorHandler == null) {
            synchronized (object) {
                if (errorHandler == null) {
                    return errorHandler = new ErrorHandler();
                }
            }
        }
        return errorHandler;
    }

    public void showEror(String errorResponsebody) {

        int a = Integer.parseInt(new JsonParser().parse(errorResponsebody).getAsJsonObject().get("code").toString());
        switch (a) {
            case 251: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_251, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 250: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_250, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 253: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_253, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 254: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_254, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 255: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_255, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 256: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_256, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 257: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_257, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 356: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_356, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 357: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_357, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 358: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_358, R.drawable.ic_error_outline_red_24dp);
                break;
            }

            case 289: {
                // todo clear shared preferences
                break;
            }
            default: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_290, R.drawable.ic_error_outline_red_24dp);
            }
        }
    }

    public void showInfo(int code) {
        switch (code) {
            case 150: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.info_150, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 151: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.info_151, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 152: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.info_152, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 252: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.info_252, R.drawable.ic_check_circle_orenge_24dp);
                break;

            }
            case 153: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.info_153, R.drawable.ic_error_outline_red_24dp);
                break;
            }
            case 300: {
                NotificationToast.getInstance().Show_Toast(mContext, R.string.error_300, R.drawable.ic_error_outline_red_24dp);
                break;
            }

        }
    }
}
