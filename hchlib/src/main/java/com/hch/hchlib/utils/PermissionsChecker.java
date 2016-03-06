package com.hch.hchlib.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by 奔向阳光 on 2016/2/3.
 * 权限检测类
 */
public class PermissionsChecker {

    private final Context mContext;

    public PermissionsChecker(Context context) {
        this.mContext = context.getApplicationContext();
    }

    // 判断是否缺少权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
//        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
        return true;
    }
}
