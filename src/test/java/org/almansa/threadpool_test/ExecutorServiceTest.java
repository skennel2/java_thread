package org.almansa.threadpool_test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

/**
 * @see https://www.baeldung.com/java-executor-service-tutorial
 * @see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html
 * @author imac
 *
 */
public class ExecutorServiceTest {

	@Test
	public void 팩토리메소드로_인스턴스생성() {
		// 팩토리메소드로 ExecutorService 인스턴스 생성
		// 10개의 고정된 스레드풀을 생성한다.
    	ExecutorService executorService = Executors.newFixedThreadPool(2);
    	
    	for (Integer value : Arrays.asList(1, 2, 3, 4, 5)) {
        	executorService.execute(new SomeRunnableTask(value));
		}
    	
    	executorService.shutdown();
	}
	
	@Test
	public void 직접_인스턴스생성() {
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		
		// ExecutorService의 하위 클래스인 ThreadPoolExecutor 인스턴스를 생성하였다.
    	ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queue);
    	
    	for (Integer value : Arrays.asList(1, 2, 3, 4, 5)) {
        	executorService.execute(new SomeRunnableTask(value));
		}
    	
    	executorService.shutdown();
	}
	
	
	@Test
	public void execute_메소드() {
    	ExecutorService executorService = Executors.newFixedThreadPool(2);
    	
    	for (Integer value : Arrays.asList(1, 2, 4, 5)) {
    		// submit 메소드는 Runnable, Callable을 할당할수 있고 Future타입을 리턴한다.
        	executorService.execute(new SomeRunnableTask(value));
		}
    	
    	executorService.shutdown();
	}
	
	@Test
	public void submit_메소드() throws InterruptedException, ExecutionException, TimeoutException {
    	ExecutorService executorService = Executors.newFixedThreadPool(2);
    	
    	for (Integer value : Arrays.asList(1, 2, 4, 5)) {
    		// submit 메소드는 실행결과 및 작업상태를 확인할 수 있는 Future를 제공한다.
        	Future<String> future = executorService.submit(new SomeCallableTask(value));
        	
        	System.out.println("isDone: " + future.isDone());
        	
        	// get 메소드는 결과를 가져올수 있을때까지 블락된다. (성능저하 주의)
        	// 또 cancel메소드등으로 취소된 작업의 결과를 가져오려고 하면 CancellationException을 발생시킨다.
        	System.out.println("result: " + future.get());
        	
        	// 결과값이 중요하지 않을경우 get 메소드는 타임아웃을 지정할 수도 있다.
        	// 200밀리세컨드 후에 TimeoutException을 발생시킨다. 
        	// String result = future.get(200, TimeUnit.MILLISECONDS);
        	
        	System.out.println("isDone: " + future.isDone());
        	
		}
    	
    	executorService.shutdown();
	}
	
	/**
	 * test Runnable class
	 * @author imac
	 *
	 */
	public class SomeRunnableTask implements Runnable {
		private Integer value;
		
		public SomeRunnableTask(Integer value) {
			this.value = value;
		}
		
		public void run() {
			if(value.equals(Integer.valueOf(3))) {
				throw new RuntimeException("일부로 예외발생");
			}
			System.out.println("task" + (this.value.toString()) + " Start");
		}
	}
	
	/**
	 * test Runnable class
	 * @author imac
	 *
	 */
	public class SomeCallableTask implements Callable<String> {
		private Integer value;
		
		public SomeCallableTask(Integer value) {
			this.value = value;
		}
		
		@Override
		public String call() {
			if(value.equals(Integer.valueOf(3))) {
				throw new RuntimeException("일부로 예외발생");
			}
			
			int a = 0;
			
			for (int i = 0; i < 300000; i++) {
				a += i;
				
				Integer v = Integer.valueOf(a);
				this.test(v);
				
			}
			
			System.out.println("task" + (this.value.toString()) + " Start");
			
			return this.value.toString();
		}
		
		public void test(Integer a) {
			
		}
	}
}
