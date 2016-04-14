package com.ym.xsgame;

import com.ym.xsgame.util.Api.Config;
import com.ym.xsgame.util.Api.RtApi;
import com.ym.xsgame.util.retrofit.RxUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称：xsgame
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：15/11/16 下午10:34
 * 修改人：wengyiming
 * 修改时间：15/11/16 下午10:34
 * 修改备注：
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static String TAG = "BaseActivity";
    protected Toolbar toolbar;
    protected ProgressDialog mProgressDialog = null;
    protected CompositeSubscription subscription = new CompositeSubscription();
    protected RtApi api = RxUtils.createApi(RtApi.class, Config.BASE_URL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        View decorView = getWindow().getDecorView();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ViewUtils.toggleHideyBar(decorView);
        setContentView(getLayoutRes());
        TAG = getActivity().getClass().getSimpleName();
        mProgressDialog = new ProgressDialog(getActivity());
        initToolBar();
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract Activity getActivity();

    @SuppressWarnings("ConstantConditions")
    private void initToolBar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar == null)
//            return;
//        toolbar.setTitle(null);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setLogo(R.drawable.logo);
////        toolbar.setNavigationIcon(R.drawable.logo);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
//        RefWatcher refWatcher = AppClient.getRefWatcher(getActivity());
//        refWatcher.watch(getActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscription);
    }
}
