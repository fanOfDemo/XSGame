package com.ym.xsgame;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.common.L;
import com.yw.filedownloader.BaseDownloadTask;
import com.yw.filedownloader.FileDownloadListener;
import com.yw.filedownloader.FileDownloader;
import com.yw.filedownloader.util.FileDownloadNotificationHelper;
import com.yw.filedownloader.util.FileDownloadUtils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAG = "GameDetailActivity";

    private TextView gameNameTv;
    private TextView gameDetailTv;
    private ImageView gameLogo;
    private TextView downLoadBtn;
    private ProgressBar progressBar;


    private String savePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "qdgame";

    private FileDownloadNotificationHelper notificationHelper;
    private Result.ReturnDataEntity.GameData mGameData;
    private int downloadId = 0;
    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        initGameInfo();
        downLoadBtn.setOnClickListener(this);

    }

    private void initGameInfo() {
        gameNameTv = (TextView) findViewById(R.id.gameName);
        gameDetailTv = (TextView) findViewById(R.id.gamedetail);
        downLoadBtn = (TextView) findViewById(R.id.donwload_btn);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        gameLogo = (ImageView) findViewById(R.id.gameLogo);
        mGameData = (Result.ReturnDataEntity.GameData) getIntent().getSerializableExtra("GameData");
        if (mGameData == null)
            finish();

        gameNameTv.setText(mGameData.getSname());
        gameDetailTv.setText(mGameData.getSintroduction());
        Glide.with(this)
                .load(mGameData.getSicon())
                .fitCenter()
                .override(Target.SIZE_ORIGINAL, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(gameLogo);
    }

    private void changeDownloadState(int per) {

        if (isDownloading) {
            if (per != 0) {
                downLoadBtn.setText("暂停" + per + "%");
            } else {
                downLoadBtn.setText("暂停");
            }
        } else {
            if (per != 0) {
                downLoadBtn.setText("继续" + per + "%");
            } else {
                downLoadBtn.setText("继续");
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.donwload_btn:
                if (isDownloading) {
                    FileDownloader.getImpl().pause(downloadId);
                    return;
                }
                downloadId = FileDownloader.getImpl().create(mGameData.getSurl())
                        .setPath(savePath)
                        .setListener(new FileDownloadListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                progressBar.setIndeterminate(true);
                                isDownloading = true;
                                changeDownloadState(0);
                            }

                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                progressBar.setIndeterminate(false);
                                progressBar.setMax(100);
                                int per =(int) ((float)soFarBytes / (float)task.getSmallFileTotalBytes())*100;
                                L.e(TAG,"soFarBytes:"+soFarBytes+"  totalBytes:"+totalBytes+"  per:"+per+"");
                                progressBar.setProgress(per);
                                isDownloading = true;
                                changeDownloadState(per);
                            }

                            @Override
                            protected void blockComplete(BaseDownloadTask task) {
                                isDownloading = true;
                                changeDownloadState(100);
                            }

                            @Override
                            protected void completed(BaseDownloadTask task) {
                                progressBar.setIndeterminate(false);
                                isDownloading = false;
                                int per = task.getSmallFileSoFarBytes() / task.getSmallFileTotalBytes()*100;
                                progressBar.setProgress(per);
                                changeDownloadState(per);
                            }

                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                progressBar.setIndeterminate(false);
                                isDownloading = false;
                                int per = soFarBytes / totalBytes*100;
                                progressBar.setProgress(per);
                                changeDownloadState(per);
                            }

                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {
                                isDownloading = false;
                                int per = task.getSmallFileSoFarBytes() / task.getSmallFileTotalBytes()*100;
                                changeDownloadState(per);
                            }

                            @Override
                            protected void warn(BaseDownloadTask task) {
                                isDownloading = false;
                                int per = task.getSmallFileSoFarBytes() / task.getSmallFileTotalBytes()*100;
                                changeDownloadState(per);
                            }
                        })
                        .start();
                break;
        }
    }
}
