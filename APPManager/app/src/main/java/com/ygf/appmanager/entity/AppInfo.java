package com.ygf.appmanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by 杨高峰 on 2015-12-22.
 */
public class AppInfo {
    public String packageName;
    public String versionName;
    public int versionCode;
    public long firstInstallTime;
    public long lastUpdateTime;
    public String AppName;
    public Drawable icon;
    public long byteSize;
    public String size;

    @Override
    public String toString() {
        return "\nAPPInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", firstInstallTime=" + firstInstallTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", AppName='" + AppName + '\'' +
                ", icon=" + icon +
                ", byteSize=" + byteSize +
                ", size='" + size + '\'' +
                '}';
    }
}
