package com.ym.xsgame.widget.AutoScrollViewPager;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 项目名称：ScanBuy
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：15/10/2 上午9:52
 * 修改人：wengyiming
 * 修改时间：15/10/2 上午9:52
 * 修改备注：
 */
public class AutoScrollFactorScroller extends Scroller {


    private double factor = 1;


    public AutoScrollFactorScroller(Context context) {
        super(context);
    }


    public AutoScrollFactorScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }


    public void setFactor(double factor) {
        this.factor = factor;
    }


    public double getFactor() {
        return factor;
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)(duration * factor));
    }
} 
