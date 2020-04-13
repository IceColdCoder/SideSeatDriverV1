package sExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SExecutor{
	
	//Number of threads for executor pool.
	private static int NUM_THREADS = 20;
	
	//Thread Executor.
	private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(NUM_THREADS);
	
	/**
	 * Schedule runnable task. For main threads.
	 * Delay = 0, interval = 1L, TimeUnit = ms.
	 * @param runnable
	 */
	public static void scheduleAtFixedRate(Runnable runnable){
		exec.scheduleAtFixedRate(runnable, 0, 1L, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedule Callable<V> to run after 1ms. To allow running threads to clear.
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> Callable<V> submit(Callable<V> c){
		return (Callable<V>)exec.schedule(c, 1L, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Method to shut down executor service. Required call.
	 */
	public static void Shutdown(){
		exec.shutdown();
	}
	
	

}
