package com.i5i58.live.common.utils.countDown;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by LGY on 2016/11/28.
 */
public class LCountDownTimer {
    public static final int PREPARE = 0;
    public static final int START = 1;
    public static final int PASUSE = 2;
    public static final int RESUME = 3;
    private long countDownInterval = 1;//时间间隔
    private int min = 1000;// 1秒
    private long distination_total = min*60;//倒计时的总数，这是不变的
    private long timer_couting = min*60;//要被减少的倒计时
    private static int timerStatus = LCountDownTimer.PREPARE;//当前状态

    private CountDownCallback callback = null;

    private Timer timer;
    private TimerTask timerTask;
    public LCountDownTimer(CountDownCallback callback)
    {
        this.callback = callback;
    }
    /**
     * start count down
     * @throws Exception The counted number is less than zero!
     */
    public void startTimer() throws Exception{
    	if (distination_total<=0)
		{
			throw new Exception("The counted number is less than zero!");
		}
    	initTimerStatus();
        timerStatus = LCountDownTimer.START;
        if(callback!=null)
            callback.start();
        countTimer();
    }

    /**
     * cancel count down
     */
    public void cancelTimer(){
        if (timer!=null)
        {
            timer.cancel();
            timer = null;
        }
    	if (timerTask!=null)
		{
        	timerTask.cancel();
        	timerTask = null;
		}
        callback.cancel();
    }
    /**
     * pause count down
     */
    public void pauseTimer(){
        if (timer==null)
            return;
        timer.cancel();
        timerTask.cancel();
        callback.pause();
        timerStatus = LCountDownTimer.PASUSE;
    }

    /**
     * resume count down
     */
    public void resumeTimer(){
        if (timer==null||timerStatus!=LCountDownTimer.PASUSE)
            return;
    	countTimer();
        callback.resume();
        timerStatus = LCountDownTimer.RESUME;
    }
    /**
     * initialize
     */
    private void initTimerStatus(){
    	if (timer!=null)
		{
            timer.cancel();
            timer = null;
		}
    	if (timerTask!=null)
		{
        	timerTask.cancel();
        	timerTask = null;
		}
        timerStatus = LCountDownTimer.PREPARE;
        timer_couting = distination_total;
    }

	public int getTimerStatus()
	{
		return timerStatus;
	}

	/**
	 * set the total time
	 * @param timer_couting
	 */
	public void setTimerCoutingMin(long timer_couting)
	{
		this.timer_couting = timer_couting*min;
		this.distination_total = timer_couting*min;
	}

    /**
     * set the total time
     * @param timer_couting
     */
	public void setTimerCoutingMm(long timer_couting)
	{
		this.timer_couting = timer_couting;
		this.distination_total = timer_couting;
	}


	/**
	 * Administrator
	 * 2017-1-9
	 *TODO set the countDown Interval
	 * @param countDownInterval
	 */
	public void setCountDownInterval(long countDownInterval)
	{
		this.countDownInterval = countDownInterval*min;
	}

	/**
	 * Administrator
	 * 2017-1-9
	 *TODO set the countDown Interval
	 * @param countDownInterval
	 */
	public void setCountDownIntervalMm(long countDownInterval)
	{
		this.countDownInterval = countDownInterval;
	}
	/**
	 * counting
	 */
	private void countTimer()
	{
        timer = new Timer();
        timerTask = new TimerTask()
        {
            @Override
            public void run() {

                if(timer_couting<=0){
                	callback.end();
                    initTimerStatus();
                    return;
                }
                if(callback!=null)
                    callback.countDowning(timer_couting);
                timer_couting -=countDownInterval;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, countDownInterval);
	}
	
	
}
