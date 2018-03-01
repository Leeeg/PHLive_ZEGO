package com.i5i58.live.common.utils.downLoad.fileDownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;

/**
 * Created by Lee on 2017/4/27.
 */

public interface FileDownLoaderCallBack {
    void downLoadCompleted(BaseDownloadTask task);
    void downLoadError(BaseDownloadTask task, Throwable e);
    void downLoadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes);
}
