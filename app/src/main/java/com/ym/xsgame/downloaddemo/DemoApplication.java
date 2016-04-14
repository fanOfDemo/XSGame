package com.ym.xsgame.downloaddemo;

import com.yw.filedownloader.FileDownloader;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jacksgong on 12/17/15.
 */
public class DemoApplication extends Application {
    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        // for demo.
        CONTEXT = this;


        /**
         * just for cache Application's Context, and ':filedownloader' progress will NOT be launched
         * by below code, so please do not worry about performance.
         * @see FileDownloader#init(Context)
         */

    }
}
