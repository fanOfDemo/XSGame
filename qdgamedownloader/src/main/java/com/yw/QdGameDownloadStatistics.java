package com.yw;

import com.yw.filedownloader.util.FileDownloadHelper;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称：qdgamedownloader
 * 类描述：下载统计
 * 创建人：wengyiming
 * 创建时间：2016/4/15 13:32
 * 修改人：wengyiming
 * 修改时间：2016/4/15 13:32
 * 修改备注：
 */
public class QdGameDownloadStatistics {

    private final static String TAG = "QdGameDownloadStatistics";
    private static OkHttpClient okHttpClient = null;
    public final static int PLAT = 1;//平台编号（1：起点阅读app,2：QQ阅读app） 2暂时不可用
    public final static int STARTED_DOWNLOAD = 1;//开始下载
    public final static int COMPLETED_DOWNLOAD = 2;//结束下载
    public final static int START_INSTALL = 3;//开始安装
    public final static int COMPLETED_INSTALL = 4;//结束安装

    private final static String BASE_QD_VISIT_URL = "http://api.game.qidian.com/sdkdownload/visit.php";

    static {
        if (okHttpClient == null) {
            okHttpClient = FileDownloadHelper.getOkHttpClient();
        }
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
    }

    /**
     * @param ty          /类型(1：开始下载；2：结束下载；3：开始安装；4：结束安装)
     * @param gameId      游戏ID
     * @param packageName 包名
     * @param userId      用户ID（登录状态 需要，非登录状态可为空）
     */
    public static Response request(final int ty, final int gameId, final String packageName, final String userId) {
        final Response[] response = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(BASE_QD_VISIT_URL + "?" + "gameid=" + gameId + "=&pid=" + packageName + "&plat=" + PLAT + "&ty=" + ty + "&userid=" + userId)
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Content-Encoding ", "gzip")
                        .addHeader("Content-Type", "charset=UTF-8")
                        .addHeader("Content-Type", "text/html")
                        .build();
                try {
                    response[0] = okHttpClient.newCall(request).execute();
                    Log.e(TAG, String.valueOf(response[0].code()));
                    Log.e(TAG, response[0].body().string());//此处注意toString()和string()方法的区别
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return response[0];
    }

}
