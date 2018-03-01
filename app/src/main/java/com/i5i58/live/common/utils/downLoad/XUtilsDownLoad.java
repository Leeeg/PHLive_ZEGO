package com.i5i58.live.common.utils.downLoad;

import android.widget.Toast;

import com.i5i58.live.model.entity.sys.SystemCache;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by Lee on 2017/4/11.
 */

public class XUtilsDownLoad {

    public static void downloadOneFile(final String url, String path, final XUtilsDownLoadCallback xUtilsDownLoadCallback) {
//        progressDialog = new ProgressDialog(this);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progressDialog.setMessage("亲，努力下载中。。。");
//                progressDialog.show();
//                progressDialog.setMax((int) total);
//                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                xUtilsDownLoadCallback.success();
//                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(SystemCache.getContext(), "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

}
