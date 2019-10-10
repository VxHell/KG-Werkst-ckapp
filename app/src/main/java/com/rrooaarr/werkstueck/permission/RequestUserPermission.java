package com.rrooaarr.werkstueck.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class RequestUserPermission {

    private Activity activity;
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_INTERNET = 2;
    private static String[] CAMERA = {
            Manifest.permission.CAMERA
    };

    private static String[] INTERNET = {
            Manifest.permission.INTERNET,
    };

    public RequestUserPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkCameraSelfPermission(){
        int permission = checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {

        } else {
            // we already have permissions!
            return true;
        }
        return false;
    }

    public void requestCameraPermissionFragment(){
        requestPermissions(
                activity,
                CAMERA,
                REQUEST_CAMERA
        );
    }

    public boolean verifyCameraPermissionsFragment() {
        // Check if we have write permission
        int permission = checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(
                    activity,
                    CAMERA,
                    REQUEST_CAMERA
            );
        } else {
            // we already have permissions!
            return true;
        }
        return false;
    }

    public boolean verifyCameraPermissionsActivity() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    CAMERA,
                    REQUEST_CAMERA
            );
        } else {
            // we already have permissions!
            return true;
        }
        return false;
    }

    public boolean verifyInternetPermissionsActivity() {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    INTERNET,
                    REQUEST_INTERNET
            );
        } else {
            // we already have permissions!
            return true;
        }
        return false;
    }

}