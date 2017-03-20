package com.flippey.photopicker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限判断
 * Created by tianshutong on 16/3/30.
 */
public class PermissionUtil {

    public static boolean isReadCamera(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReadStorage(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWriteStorage(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReadRecord(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReadContact(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReadPhone(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCallPhone(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCoaseLocation(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isInternet(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isAlertWindow(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isGetAccounts(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isSetDebugApp(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.SET_DEBUG_APP) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReadLogs(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_LOGS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求权限
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions,requestCode);
    }

}
