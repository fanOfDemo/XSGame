package com.ym.xsgame.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * 项目名称：ScanBuy
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：15/12/9 下午10:06
 * 修改人：wengyiming
 * 修改时间：15/12/9 下午10:06
 * 修改备注：
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}