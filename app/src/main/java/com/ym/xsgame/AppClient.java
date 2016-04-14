package com.ym.xsgame;

import com.ym.xsgame.util.common.L;
import com.ym.xsgame.util.common.SDCardUtils;
import com.yw.filedownloader.FileDownloader;
import com.yw.filedownloader.util.FileDownloadHelper;
import com.yw.filedownloader.util.FileDownloadUtils;

import android.app.Application;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

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
        L.isDebug = true;
        sInstance = this;
        /**
         * 每10 毫秒抛最多1个Message到ui线程，并且每次抛到ui线程后，
         * 在ui线程最多处理处理5个回调。
         * <p/>
         */
        FileDownloader.enableAvoidDropFrame();//避免掉帧
        FileDownloader.setGlobalHandleSubPackageSize(5);//DEFAULT_SUB_PACKAGE_SIZE=5
        FileDownloadUtils.setDefaultSaveRootPath(SDCardUtils.getSDCardPath());//设置默认下载地址为sd跟目录
        FileDownloader.init(getApplicationContext(),
                new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
                    @Override
                    public OkHttpClient customMake() {
                        // just for OkHttpClient customize.
                        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        // you can set the connection timeout.
                        builder.connectTimeout(15_000, TimeUnit.MILLISECONDS);
                        // you can set the HTTP proxy.
                        builder.proxy(Proxy.NO_PROXY);
                        // etc.
                        return builder.build();
                    }
                });
//        refWatcher = LeakCanary.install(sInstance);
    }


}
