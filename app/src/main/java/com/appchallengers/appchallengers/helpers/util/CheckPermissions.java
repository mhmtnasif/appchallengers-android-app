package com.appchallengers.appchallengers.helpers.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;


public class CheckPermissions {

    private final int REQUEST_CODE_STORAGE_PERMS = 321;
    private static CheckPermissions checkPermissions = null;
    private static Object lock = new Object();
    private Activity mActivity;
    private String[] mPermissionsString;

    private CheckPermissions() {
    }

    public static CheckPermissions getInstance() {
        if (checkPermissions == null) {
            synchronized (lock) {
                if (checkPermissions == null)
                    checkPermissions = new CheckPermissions();
            }
        }
        return checkPermissions;
    }

    @SuppressLint("WrongConstant")
    public boolean hasPermissions(Activity activity, String[] permission) {
        int res = 0;
        mActivity = activity;
        mPermissionsString = permission;
        for (String perms : mPermissionsString) {
            res = mActivity.checkCallingOrSelfPermission(perms);
            if (!(res == activity.getPackageManager().PERMISSION_GRANTED)) {
                requestNecessaryPermissions();
                return false;
            }

        }
        return true;
    }

    private void requestNecessaryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.requestPermissions(mPermissionsString, REQUEST_CODE_STORAGE_PERMS);
        }
    }

}
