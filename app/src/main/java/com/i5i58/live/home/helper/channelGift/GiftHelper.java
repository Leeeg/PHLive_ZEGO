package com.i5i58.live.home.helper.channelGift;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by admin on 2017/8/17.
 */

public class GiftHelper {
    private static GiftHelper sInstance = new GiftHelper();

    private HandlerThread _backgroundThread = new HandlerThread("zego_background");
    private Handler backgroundHandler;

    private GiftHelper() {
        _backgroundThread.start();
        backgroundHandler = new Handler(_backgroundThread.getLooper());
    }

    /**
     * 在后台执行一个任务，相比普通 Thread，可以对所有待执行的任务排序
     *
     * @param runnable
     */
    static public void postTask(Runnable runnable) {
        sInstance.backgroundHandler.post(runnable);
    }

    /**
     * 移除所有后台任务
     */
    static public void removeTask(Runnable runnable) {
        sInstance.backgroundHandler.removeCallbacks(runnable);
    }

}
