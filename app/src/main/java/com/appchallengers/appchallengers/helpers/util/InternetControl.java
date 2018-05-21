package com.appchallengers.appchallengers.helpers.util;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import android.widget.FrameLayout;

import com.appchallengers.appchallengers.R;

public class InternetControl {
    public static InternetControl internetControl = null;
    public static Object object = new Object();

    public boolean InternetControl(){
        if(!ConnectivityReceiver.isConnected()){
            return false;
        }
        else {
            return true;
        }

    }

    public static InternetControl getInstance() {
        if (internetControl == null) {
            synchronized (object) {
                if (internetControl == null) {
                    return internetControl = new InternetControl();
                }
            }
        }
        return internetControl;
    }

    public void showSnack(View view) {
        int message;
        int color;
        if (!ConnectivityReceiver.isConnected()) {
            message = R.string.activities_internet_control_message;
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(view, message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();

        } else {

        }

    }
    public void showSnackGeneral(FrameLayout frameLayout, boolean isConnected) {
        int message;
        int color;
        if (!isConnected) {
            message = R.string.activities_internet_control_message;
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(frameLayout, message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        } else {

        }

    }
}
