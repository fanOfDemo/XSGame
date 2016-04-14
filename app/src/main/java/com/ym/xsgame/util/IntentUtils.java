package com.ym.xsgame.util;

import com.ym.xsgame.GameDetailActivity;
import com.ym.xsgame.WebViewActivity;
import com.ym.xsgame.po.Result;

import android.content.Context;
import android.content.Intent;

/**
 * 项目名称：railtool
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：15/11/16 下午10:34
 * 修改人：wengyiming
 * 修改时间：15/11/16 下午10:34
 * 修改备注：
 */
public class IntentUtils {
    private final static int NO_ANIMOTION = -1;


    public static void enterWebViewActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    public static void enterGameDetailActivity(Context context, Result.ReturnDataEntity.GameData gameData) {
        Intent intent = new Intent(context, GameDetailActivity.class);
        intent.putExtra("GameData", gameData);
        context.startActivity(intent);
    }
}
