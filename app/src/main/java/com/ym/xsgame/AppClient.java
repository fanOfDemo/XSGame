package com.ym.xsgame;

import com.ym.xsgame.util.common.L;
import com.ym.xsgame.util.common.SDCardUtils;
import com.yw.filedownloader.BaseDownloadTask;
import com.yw.filedownloader.FileDownloadListener;
import com.yw.filedownloader.FileDownloadMonitor;
import com.yw.filedownloader.FileDownloader;
import com.yw.filedownloader.util.FileDownloadUtils;

import android.app.Application;

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
        FileDownloader.init(sInstance);

        /**
         * 每10 毫秒抛最多1个Message到ui线程，并且每次抛到ui线程后，
         * 在ui线程最多处理处理5个回调。
         * <p/>
         */
        FileDownloader.enableAvoidDropFrame();//避免掉帧
        FileDownloader.setGlobalHandleSubPackageSize(5);//DEFAULT_SUB_PACKAGE_SIZE=5
        FileDownloadUtils.setDefaultSaveRootPath(SDCardUtils.getSDCardPath());//设置默认下载地址为sd跟目录
        /**
         * DEBUG 全局监听
         */
        FileDownloadMonitor.setGlobalMonitor(new FileDownloadMonitor.IMonitor() {
            @Override
            public void onRequestStart(int count, boolean serial, FileDownloadListener lis) {
            //将会在启动队列任务是回调这个方法
            }

            @Override
            public void onRequestStart(BaseDownloadTask task) {
            //将会在启动单一任务时回调这个方法
            }

            @Override
            public void onTaskBegin(BaseDownloadTask task) {
            //将会在内部接收并开始task的时候回调这个方法(会在pending回调之前)
            }

            @Override
            public void onTaskStarted(BaseDownloadTask task) {

            }

            @Override
            public void onTaskOver(BaseDownloadTask task) {
            //将会在task走完所有生命周期是回调这个方法
            }
        });
//        refWatcher = LeakCanary.install(sInstance);
    }


}
