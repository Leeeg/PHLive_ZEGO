package com.i5i58.live.common.utils.countDown;

/**
 * Created by LGY on 2016/11/28.
 */
public interface CountDownCallback {
    public void start();
    public void pause();
    public void resume();
    public void cancel();
    public void countDowning(long count);
    public void end();
}
