package com.ygf.appmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.ygf.appmanager.entity.AppInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015-12-22.
 */
public class Utils {

    public static List<AppInfo> getAppInfos(Context context){

        List<AppInfo> list = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> pList = pm.getInstalledPackages(0);
        for (int i= 0; i<pList.size(); i++){
            PackageInfo packageInfo = pList.get(i);
            if(isThirdPartyApp(packageInfo.applicationInfo)
                    &&!packageInfo.packageName.equals(context.getPackageName())){
                AppInfo appInfo = new AppInfo();
                appInfo.packageName = packageInfo.packageName;
                appInfo.versionName = packageInfo.versionName;
                appInfo.versionCode = packageInfo.versionCode;
                appInfo.firstInstallTime = packageInfo.firstInstallTime;
                appInfo.lastUpdateTime = packageInfo.lastUpdateTime;

                appInfo.AppName = (String) packageInfo.applicationInfo.loadLabel(pm);
                appInfo.icon = packageInfo.applicationInfo.loadIcon(pm);

                String dir = packageInfo.applicationInfo.publicSourceDir;
                long byteSize = new File(dir).length();
                appInfo.byteSize = byteSize;
                appInfo.size = getSize(byteSize);

                list.add(appInfo);
            }
        }
        return list;
    };
    /**
     * 字节--> Mb, 保留两位小数
     * @param size
     * @return
     */
    public static String getSize(long size) {
        return new DecimalFormat("0.##").format(size * 1.0 / (1024 * 1024));
    }

    public static String getTime(long millis){
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 判断应用是否是第三方应用
     * @param appInfo
     * @return
     */
    public static boolean isThirdPartyApp(ApplicationInfo appInfo) {
        boolean flag = false;
        if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            // 可更新的系统应用
            flag = true;
        } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // 非系统应用
            flag = true;
        }
        return flag;
    }

    /**
     * 打开应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean openPackage(Context context, String packageName) {

        try {
            Intent intent =// 获取可以启动该应用的意图
                    context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在新的进程里启动
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void uninstallApk(Activity context, String packageName,
                                    int requestCode){
        Uri uri= Uri.parse("package:"+packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE,uri);
        context.startActivityForResult(intent,requestCode);

    }

    /**
     * 返回搜索集合
     * @param list
     * @param keyword 关键字
     * @return
     */
    public static List<AppInfo> getSearchResult(List<AppInfo> list, String keyword){

        // 返回结果集合
        List<AppInfo> searchResultList = new ArrayList<AppInfo>();
        for (int i = 0; i < list.size(); i++) {
            AppInfo app = list.get(i);
            if (app.AppName.toLowerCase().contains(keyword.toLowerCase())) {
                searchResultList.add(app);
            }
        }
        return searchResultList;
    }

    public static SpannableStringBuilder highLightText(String str, String key){
        int start = str.toLowerCase().indexOf(key.toLowerCase());// 不区分大小写
        int end = start + key.length();

        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(
                new ForegroundColorSpan(Color.RED),// 前景颜色
                start,// 起始坐标
                end,// 终止坐标
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );

        return sb;
    }

}
