package com.ym.xsgame;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.common.AppUtils;
import com.ym.xsgame.util.common.L;
import com.ym.xsgame.util.common.NetUtils;
import com.yw.filedownloader.BaseDownloadTask;
import com.yw.filedownloader.FileDownloadListener;
import com.yw.filedownloader.FileDownloader;
import com.yw.filedownloader.util.FileDownloadUtils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAG = "GameDetailActivity";

    private TextView gameNameTv;
    private TextView gameDetailTv;
    private ImageView gameLogo;
    private TextView downLoadBtn;
    private ProgressBar progressBar;


    private String savePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "Download";
    private String filePath;
    private Result.ReturnDataEntity.GameData mGameData;
    private int downloadId = 0;
    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        initGameInfo();
        downLoadBtn.setOnClickListener(this);

        File file = new File(savePath);
        if (file.exists()) {
            changeDownloadState(0);
        } else {
            downLoadBtn.setText("开始");
        }

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

        filePath = savePath + File.separator + mGameData.getSname() + ".apk";
        File file = new File(filePath);
        if (file.exists()) {
            downLoadBtn.setText("下载");
        }
        /**
         * TODO
         * 检查文件是否存在
         * 获取文件大小并恢复进度
         * 设置未非下载状态
         *
         *
         */

        cotinuDownload();
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
                cotinuDownload();
                break;
        }
    }

    private void cotinuDownload() {
        if (isDownloading) {
            FileDownloader.getImpl().pause(downloadId);
            isDownloading = false;
            return;
        }

        if (!NetUtils.isWifi(this)) {
            Toast.makeText(GameDetailActivity.this, "请在wifi网络下下载大文件游戏", Toast.LENGTH_SHORT).show();
        }
        downloadId = FileDownloader.getImpl().create(mGameData.getSurl())
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressBar.setIndeterminate(true);
                        isDownloading = true;
                        float sofar = soFarBytes / 100;
                        float total = totalBytes / 100;
                        int per = (int) ((sofar / total) * 100);
                        changeDownloadState(per);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressBar.setIndeterminate(false);
                        progressBar.setMax(totalBytes);
                        float sofar = soFarBytes / 100;
                        float total = totalBytes / 100;
                        int per = (int) ((sofar / total) * 100);
                        L.e(TAG, "soFarBytes:" + soFarBytes + "  totalBytes:" + totalBytes + "  per:" + per + "");
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
                        float sofar = task.getSmallFileSoFarBytes() / 100;
                        float total = task.getSmallFileTotalBytes() / 100;
                        int per = (int) ((sofar / total) * 100);
                        progressBar.setProgress(per);
                        changeDownloadState(per);


                        L.e(TAG, task.getPath());
                        AppUtils.install(GameDetailActivity.this, task.getPath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressBar.setIndeterminate(false);
                        isDownloading = false;
                        float sofar = soFarBytes / 100;
                        float total = totalBytes / 100;
                        int per = (int) ((sofar / total) * 100);
                        progressBar.setProgress(per);
                        changeDownloadState(per);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        isDownloading = false;
                        L.e(TAG, e.toString());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        isDownloading = false;
                    }
                })
                .start();
    }


}
