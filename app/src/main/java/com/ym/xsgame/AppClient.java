package com.ym.xsgame;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ym.xsgame.util.FrescoConfig;
import com.ym.xsgame.util.common.L;

/**
 * 项目名称：
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
//        PgyCrashManager.register(sInstance);
//        refWatcher = LeakCanary.install(sInstance);
        Fresco.initialize(sInstance, FrescoConfig.getImagePipelineConfig(sInstance));
    }




}
