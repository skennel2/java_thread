package org.almansa.threadpool_test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.junit.Test;

/**
 * https://codechacha.com/ko/java-fork-join-pool/
 * @author imac
 *
 */
public class ForkJoinTest {

	@Test
	public void test() {
		List<String> items = Arrays.<String>asList("123", "2323", "3434");
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(4);
		int result = forkJoinPool.invoke(new RecusiveTaskTest(items));

		System.out.println(result);
	}
	
	public class RecusiveTaskTest extends RecursiveTask<Integer> {

		private static final long serialVersionUID = 1L;
		
		private List<String> value;
		
		public RecusiveTaskTest(List<String> value) {
			// TODO Auto-generated constructor stub
			this.value = value;
		}
		
		
		@Override
		protected Integer compute() {
			return this.value.stream().map(a -> a.length()).reduce(Integer::sum).orElse(null);
		}
	}
}