package com.ym.xsgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.ym.xsgame.adapter.GameListAdapter;
import com.ym.xsgame.po.Bander;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.common.GlobalUtils;
import com.ym.xsgame.util.common.L;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {
    @Bind(R.id.list)
    SuperRecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private SparseItemRemoveAnimator mSparseAnimator;
    private Handler mHandler;
    private GameListAdapter fastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
//        mRecycler.setupSwipeToDismiss(this);
//        mSparseAnimator = new SparseItemRemoveAnimator();
//        mRecycler.getRecyclerView().setItemAnimator(mSparseAnimator);
        mHandler = new Handler(Looper.getMainLooper());
        fastAdapter = new GameListAdapter(new ArrayList<Bander.ReturnDataEntity.DataEntity>(), new ArrayList<Result.ReturnDataEntity.DataEntity>());
        mRecycler.setAdapter(fastAdapter);

        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setupMoreListener(this, 1);

        getBanner();
        getList();
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
                getBanner();
                curPage = 1;
                getList();
            }
        }, 500);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                curPage++;
                getList();
            }
        }, 500);
    }


    private int curPage = 1;
    private int pageType = 1;
    private int pageSize = 10;

    private void getList() {
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
                        fastAdapter.addAll(t.getData());
                        fastAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e("getGameList" + throwable.toString());
                        GlobalUtils.showToastShort(MainActivity.this, getString(R.string.net_error));
                    }
                }));
    }

    private int curBanerPage = 1;

    private void getBanner() {
        subscription.add(api.getBanner("adlistforindex", curBanerPage, 1, 4)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Bander, Bander.ReturnDataEntity>() {
                    @Override
                    public Bander.ReturnDataEntity call(Bander t) {
                        L.e("getBanner： " + t.getReturnCode() + t.getReturnMessage());
                        if (t.getReturnCode() == -1) {
                            L.e("getBanner： " + t.getReturnCode() + t.getReturnMessage());
                            GlobalUtils.showToastShort(AppClient.getInstance(), getString(R.string.net_error));
                            return null;
                        }
                        GlobalUtils.showToastShort(AppClient.getInstance(), t.getReturnMessage());
                        return t.getReturnData();
                    }
                })
                .subscribe(new Action1<Bander.ReturnDataEntity>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void call(Bander.ReturnDataEntity t) {
                        if (t == null) return;
                        L.e("getBanner size： " + t.getData().size());
                        if (t.getData().size() <= 0) {
                            GlobalUtils.showToastShort(AppClient.getInstance(), "no more datas");
                            return;
                        }
                        fastAdapter.setBanner(t.getData());
                        fastAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e("getBanner" + throwable.toString());
                        GlobalUtils.showToastShort(MainActivity.this, getString(R.string.net_error));
                    }
                }));
    }

}
