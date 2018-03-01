package com.i5i58.live.common.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private static ThreadPool p = null;
	private static ExecutorService service;

	public static synchronized ThreadPool getInstance() {//线程安全的单例模式
		if (p == null) {
			p = new ThreadPool();
			//获取cpu的数量
			int cpuNum=Runtime.getRuntime().availableProcessors();
			//固定创建线程
			service=Executors.newFixedThreadPool(cpuNum*2);
		}
		return p;
	}
	/**
	 * 执行我传进来的任�?
	 * @param run
	 */
	public void addTask(Runnable run){
		service.execute(run);
	}
}
