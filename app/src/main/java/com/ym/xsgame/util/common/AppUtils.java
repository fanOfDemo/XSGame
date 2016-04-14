package com.ym.xsgame.util.common;

import com.ym.xsgame.AppClient;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：railtool
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：15/11/16 下午10:34
 * 修改人：wengyiming
 * 修改时间：15/11/16 下午10:34
 * 修改备注：
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = AppClient.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    AppClient.getInstance().getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown version";
    }


    /**
     * @return the version code of the application
     */
    public static int getVersionCode() {
        if (AppClient.getInstance() == null) {
            return -1;
        }

        int versionCode = -1;
        try {
            PackageManager packageMng = AppClient.getInstance().getPackageManager();
            if (packageMng != null) {
                PackageInfo packageInfo = packageMng.getPackageInfo(AppClient.getInstance().getPackageName(), 0);
                if (packageInfo != null) {
                    versionCode = packageInfo.versionCode;
                }
            }
        } catch (NameNotFoundException ignored) {
        }

        return versionCode;
    }

    public static boolean checkVersion(String vc) {
        String localVc = AppUtils.getVersionName();
        if (TextUtils.equals("unknown version", localVc)) {
            return false;
        }
        L.e("localVc" + localVc);
        if (!TextUtils.isEmpty(localVc)) {
            return false;
        }
        if (localVc.contains(".")) {
            localVc = localVc.replace(".", "");
        }
        try {
            int newVcInt = Integer.parseInt(vc);
            int localVcInt = Integer.parseInt(localVc);
            return newVcInt > localVcInt;
        } catch (NumberFormatException ignored) {

        }
        return false;
    }


    /**
     * 获取机器唯一标识
     *
     * @return String
     */
    public static String getLocaldeviceId() {
        TelephonyManager tm = (TelephonyManager) AppClient.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null
                || deviceId.trim().length() == 0) {
            deviceId = String.valueOf(System
                    .currentTimeMillis());
        }
        return deviceId;
    }

    /**
     * 获取mac地址
     *
     * @return String
     */
    public static String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) AppClient.getInstance().getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * @return the ANDROID_ID that identify the device, or the "emulator" string
     * on the emulator.
     */
    public static String getAndroidId() {
        String androidId = Settings.Secure.getString(AppClient.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId == null || androidId.length() <= 0) {
            androidId = "emulator";
        }
        return androidId;
    }


    public static HashMap<String, String> getMap(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        L.e(map.toString());
        return map;
    }

    /**
     * 远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
     * 本地文件	file://	FileInputStream
     * Content provider	content://	ContentResolver
     * asset目录下的资源
     *
     * @param res https://github.com/facebook/fresco/issues/257
     * @return Uri
     */
    public static Uri getResFrescoUri(int res) {
        String path = "res:/" + res;
        L.e(path);
        return Uri.parse(path);
    }

    public static Uri getFileFrescoUri(String fileName) {

        String path = SDCardUtils.getSDCardPath() + fileName;
        if (path.contains(".png")) {
            path = path.replace(".png", ".jpg");
        }
        L.e(path);
        return Uri.parse("file://" + path);
    }

    public static Uri getContentResolverFrescoUri(String contentResolver) {
        return Uri.parse("content://" + contentResolver);
    }

    public static Uri getassetFrescoUri(int asset) {
        return Uri.parse("asset://" + asset);
    }

    public static String getVideoPath(String fileName) {
        return SDCardUtils.getSDCardPath() + fileName;
    }


    public static void callPhone(Activity ctx, String number) {
        //用intent启动拨打电话  
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            GlobalUtils.showToastShort(ctx, "拨打电话权限被拒");
            return;
        }
        ctx.startActivity(intent);
    }

    public static boolean checkInstalled(Context ctx, String appName) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(appName);
    }


    public static void openGame(Context ctx, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = ctx.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = ctx.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            ctx.startActivity(intent);
        }
    }


    public  static  void install(Context ctx, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

}
