package com.ym.xsgame;

import com.ym.xsgame.util.common.SDCardUtils;
import com.ym.xsgame.util.retrofit.OkHttpClientManager;
import com.yw.filedownloader.FileDownloader;
import com.yw.filedownloader.util.FileDownloadHelper;
import com.yw.filedownloader.util.FileDownloadLog;
import com.yw.filedownloader.util.FileDownloadUtils;

import android.app.Application;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * 项目名称：xsgame
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：16/01/06 下午10:34
 * 修改人：wengyiming
 * 修改时间：16/01/06 下午10:34
 * 修改备注：
 */
public class AppClient extends Application {
    private static AppClient sInstance;

//    private RefWatcher refWatcher;

    public static AppClient getInstance() {
        return sInstance;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        AppClient application = (AppClient) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FileDownloadLog.NEED_LOG = true;
        FileDownloader.init(getApplicationContext(),
                new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
                    @Override
                    public OkHttpClient customMake() {
                        return OkHttpClientManager.getInstance();
                    }
                });
        /**
         * 每10 毫秒抛最多1个Message到ui线程，并且每次抛到ui线程后，
         * 在ui线程最多处理处理5个回调。
         * 设置默认下载目录
         */
        FileDownloader.enableAvoidDropFrame();
        FileDownloader.setGlobalHandleSubPackageSize(5);
        FileDownloadUtils.setDefaultSaveRootPath(SDCardUtils.getSDCardPath() + File.separator + "QdGameDownload");//设置默认下载地址为sd跟目录
//        refWatcher = LeakCanary.install(sInstance);
    }


}
