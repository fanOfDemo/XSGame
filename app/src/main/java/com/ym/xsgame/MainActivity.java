package com.ym.xsgame;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.ym.xsgame.adapter.GameListAdapter;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.IntentUtils;
import com.ym.xsgame.util.common.AppUtils;
import com.ym.xsgame.util.common.GlobalUtils;
import com.ym.xsgame.util.common.L;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener, GameListAdapter.OnGameClickListener {
    SuperRecyclerView mRecycler;
    private Handler mHandler;
    private GameListAdapter mGameListAdapter;
    private int curPage = 1;
    private int pageType = 1;
    private int pageSize = 10;
    private int curBanerPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecycler = (SuperRecyclerView) findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mHandler = new Handler(Looper.getMainLooper());
        mGameListAdapter = new GameListAdapter(new ArrayList<Result.ReturnDataEntity.GameData>());
        mRecycler.setAdapter(mGameListAdapter);
        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setupMoreListener(this, 1);
        mGameListAdapter.setGameClickListener(this);
        getGameList();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                curBanerPage++;
                curPage = 1;
                getGameList();
            }
        }, 500);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                curPage++;
                getGameList();
            }
        }, 500);
    }


    private void getGameList() {
        subscription.add(api.getGameList(pageType, 0, pageSize, curPage)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Result, Result.ReturnDataEntity>() {
                    @Override
                    public Result.ReturnDataEntity call(Result t) {
                        L.e("getGameList1： " + t.getReturnCode() + t.getReturnMessage());
                        if (t.getReturnCode() == -1) {
                            L.e("getGameList2： " + t.getReturnCode() + t.getReturnMessage());
                            GlobalUtils.showToastShort(AppClient.getInstance(), getString(R.string.net_error));
                            return null;
                        }
                        GlobalUtils.showToastShort(AppClient.getInstance(), t.getReturnMessage());
                        return t.getReturnData();
                    }
                })
                .subscribe(new Action1<Result.ReturnDataEntity>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void call(Result.ReturnDataEntity t) {
                        if (t == null) return;
                        L.e("getGameList size： " + t.getData().size());
                        if (t.getData().size() <= 0) {
                            GlobalUtils.showToastShort(AppClient.getInstance(), "no more datas");
                            mRecycler.hideMoreProgress();
                            return;
                        }
                        mGameListAdapter.addAll(t.getData());
                        mGameListAdapter.notifyDataSetChanged();


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e("getGameList" + throwable.toString());
                        GlobalUtils.showToastShort(MainActivity.this, getString(R.string.net_error));
                    }
                }));
    }


    @Override
    public void onPageGameClick(Result.ReturnDataEntity.GameData gameData) {
        String url = "http://xs.qidian.com/api/syxs/sgame/begin.php?gameid=" + gameData.getIgameid() + "&ty=0";
        IntentUtils.enterWebViewActivity(this, gameData.getSname(), url);
    }

    @Override
    public void onApkGameOpenClick(Result.ReturnDataEntity.GameData gameData) {
        AppUtils.openGame(this, gameData.getSpackage());
    }

    @Override
    public void onApkGameDownLoadClick(Result.ReturnDataEntity.GameData gameData) {
        IntentUtils.enterGameDetailActivity(this,gameData);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void gotoCompleteDemo() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, com.ym.xsgame.downloaddemo.MainActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }

}
