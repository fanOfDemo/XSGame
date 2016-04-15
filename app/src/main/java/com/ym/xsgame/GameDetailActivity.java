package com.ym.xsgame;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.common.AppUtils;
import com.ym.xsgame.util.common.L;
import com.ym.xsgame.util.common.NetUtils;
import com.yw.QdGameDownloadStatistics;
import com.yw.filedownloader.BaseDownloadTask;
import com.yw.filedownloader.FileDownloadListener;
import com.yw.filedownloader.FileDownloader;
import com.yw.filedownloader.model.FileDownloadStatus;
import com.yw.filedownloader.util.FileDownloadUtils;

import android.annotation.SuppressLint;
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


    private String savePath = FileDownloadUtils.getDefaultSaveRootPath();
    private String filePath;
    private Result.ReturnDataEntity.GameData mGameData;
    private int downloadId = 0;
    private boolean isDownloading = false;

    private String userId = "";

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

        filePath = savePath + File.separator + mGameData.getSname() + ".apk";
        downloadId = FileDownloadUtils.generateId(mGameData.getSurl(), filePath);
        progressBar.setMax(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int status = FileDownloader.getImpl().getStatus(downloadId);
        switch (status) {
            case FileDownloadStatus.pending:
                L.e(TAG, "pending");
                downloading("暂停");
                isDownloading = true;
                break;
            case FileDownloadStatus.progress:
                L.e(TAG, "progress");
                downloading("暂停");
                isDownloading = true;
                break;
            case FileDownloadStatus.blockComplete:
                L.e(TAG, "blockComplete");
                downloading("完成");
                isDownloading = true;
                break;
            case FileDownloadStatus.completed:
                L.e(TAG, "completed");
                isDownloading = true;
                if (AppUtils.checkInstalled(this, mGameData.getSpackage())) {
                    downLoadBtn.setText("打开");
                } else {
                    AppUtils.install(GameDetailActivity.this, filePath);
                }
                break;
            case FileDownloadStatus.paused:
                L.e(TAG, "paused");
                isDownloading = false;
                downloading("继续");
                break;
            case FileDownloadStatus.error:
                L.e(TAG, "error");
                isDownloading = false;
                downloading("下载");
                break;
            case FileDownloadStatus.warn:
                L.e(TAG, "warn");
                break;
            case FileDownloadStatus.connected:
                L.e(TAG, "connected");
                break;
            case FileDownloadStatus.retry:
                L.e(TAG, "retry");
                break;
            case FileDownloadStatus.started:
                L.e(TAG, "started");
                break;
            case FileDownloadStatus.INVALID_STATUS:
                L.e(TAG, "无效状态");
                if (AppUtils.checkInstalled(GameDetailActivity.this, mGameData.getSpackage())) {
                    downLoadBtn.setText("打开");
                } else {
                    downLoadBtn.setText("下载");
                }
                break;

        }

    }

    @SuppressLint("SetTextI18n")
    private void downloading(String btnState) {
        long soFarBytes = FileDownloader.getImpl().getSoFar(downloadId);
        long totalBytes = FileDownloader.getImpl().getTotal(downloadId);
        float sofar = soFarBytes / 100;
        float total = totalBytes / 100;
        int per = (int) ((sofar / total) * 100);
        progressBar.setProgress(per);
        downLoadBtn.setText(btnState + String.valueOf(per));
    }


    private void changeDownloadState(String per) {
        if (isDownloading) {
            downLoadBtn.setText("暂停" + per + "%");
        } else {
            downLoadBtn.setText("继续" + per + "%");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.donwload_btn:
                if (AppUtils.checkInstalled(this, mGameData.getSpackage())) {
                    AppUtils.openGame(GameDetailActivity.this, mGameData.getSpackage());
                } else {
                    cotinuDownload();
                }
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
            return;
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
                        changeDownloadState(String.valueOf(per));
                        /**
                         * 统计 开始下载
                         */
                        QdGameDownloadStatistics.request(QdGameDownloadStatistics.STARTED_DOWNLOAD, mGameData.getIgameid(), mGameData.getSpackage(), userId);
                    }


                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressBar.setIndeterminate(false);
                        float sofar = soFarBytes / 100;
                        float total = totalBytes / 100;
                        int per = (int) ((sofar / total) * 100);
                        L.e(TAG, "soFarBytes:" + soFarBytes + "  totalBytes:" + totalBytes + "  per:" + per + "");
                        progressBar.setProgress(per);
                        isDownloading = true;
                        changeDownloadState(String.valueOf(per));
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        isDownloading = true;
                        changeDownloadState(String.valueOf(100));
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        /**
                         * 统计 完成下载
                         */
                        QdGameDownloadStatistics.request(QdGameDownloadStatistics.COMPLETED_DOWNLOAD, mGameData.getIgameid(), mGameData.getSpackage(), userId);

                        progressBar.setIndeterminate(false);
                        isDownloading = false;
                        float sofar = task.getSmallFileSoFarBytes() / 100;
                        float total = task.getSmallFileTotalBytes() / 100;
                        int per = (int) ((sofar / total) * 100);
                        progressBar.setProgress((int) per);
                        changeDownloadState(String.valueOf(per));
                        if (AppUtils.checkInstalled(GameDetailActivity.this, mGameData.getSpackage())) {
                            downLoadBtn.setText("打开");
                            /**
                             * 统计 完成安装  这里只是实例，
                             */
                            QdGameDownloadStatistics.request(QdGameDownloadStatistics.COMPLETED_INSTALL, mGameData.getIgameid(), mGameData.getSpackage(), userId);//统计完成安装
                        } else {
                            /**
                             * 统计开始安装
                             */
                            QdGameDownloadStatistics.request(QdGameDownloadStatistics.START_INSTALL, mGameData.getIgameid(), mGameData.getSpackage(), userId);
                            AppUtils.install(GameDetailActivity.this, filePath);
                        }
                        L.e(TAG, task.getPath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progressBar.setIndeterminate(false);
                        isDownloading = false;
                        float sofar = soFarBytes / 100;
                        float total = totalBytes / 100;
                        int per = (int) ((sofar / total) * 100);
                        progressBar.setProgress((int) per);
                        changeDownloadState(String.valueOf(per));
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
