package bot.utils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Utils {

	public static void main(String[] args) {
	}

	public static void timeIt(ThrowableRunnable codeBlock) {
		try {
			long begin = System.currentTimeMillis();
			codeBlock.run();
			long end = System.currentTimeMillis();
			System.out.printf("Time taken %d ms%n", (end - begin));
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static boolean tryCatch(ThrowableRunnable throwableRunnable) {
		try {
			throwableRunnable.run();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	public static boolean tryIgnore(ThrowableRunnable throwableRunnable) {
		try {
			throwableRunnable.run();
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public static boolean tryCatch(Callable<Boolean> callable) {
		try {
			return callable.call();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static <T> T tryOrThrow(Callable<T> callable) {
		try {
			return callable.call();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static void tryOrThrow(ThrowableRunnable codeBlock) {
		try {
			codeBlock.run();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static <T> Optional<T> tryCatchGet(Callable<T> callable) {
		try {
			return Optional.ofNullable(callable.call());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static void measureTime(ThrowableRunnable codeBlock, int times) {
		try {
			int count = 1;
			long begin = System.currentTimeMillis();
			while (count++ <= times) codeBlock.run();
			long timeTaken = System.currentTimeMillis() - begin;
			double timePerOne = (double) timeTaken / times;
			System.out.printf("Time taken %d ms, Avg time per one is %f ms%n", timeTaken, timePerOne);
			System.out.println("--------------------------------------------------------------");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

	public static void startTimer() {
		threadLocal.set(System.currentTimeMillis());
	}

	public static void resetTime() {
		printTime();
		threadLocal.remove();
		System.out.println("Resetting time...");
		startTimer();
	}

	public static void printTime() {
		long millis = System.currentTimeMillis() - threadLocal.get();
		System.out.printf("Time taken %d ms%n", millis);
	}

	public static void print(boolean[][] mat) {
		System.out.println("Matrix : ");
		for (boolean[] arr : mat)
			System.out.println(Arrays.toString(arr));
		line();
	}

	public static void print(int[][] mat) {
		System.out.println("Matrix : ");
		for (int[] arr : mat)
			System.out.println(Arrays.toString(arr));
		line();
	}

	public static void print(long[] arr) {
		print(arr, String.valueOf(Arrays.hashCode(arr)));
	}

	public static void print(long[] arr, String identifier) {
		System.out.printf("Array %s : %s%n", identifier, Arrays.toString(arr));
		line();
	}

	public static void print(int[] arr) {
		print(arr, String.valueOf(Arrays.hashCode(arr)));
	}

	public static void printWithIndices(int... arr) {
		for (int i = 0; i < arr.length; i++)
			System.out.print(i + "\t");
		System.out.println();
		for (int e : arr) {
			System.out.print(e + "\t");
		}
		System.out.println();
		line();
	}

	public static int[] toArray(List<Integer> list) {
		return list.stream().mapToInt(Integer::intValue).toArray();
	}

	public static void print(int[] arr, String identifier) {
		System.out.printf("Array %s : %s%n", identifier, Arrays.toString(arr));
		line();
	}

	public static void print(char[] arr) {
		System.out.printf("Array %s : %s%n", Arrays.hashCode(arr), Arrays.toString(arr));
		line();
	}

	public static void line() {
		System.out.println("---------------------------------------------------------------------------");
	}

	public static void binary(long num) {
		String digitFormat = String.format("%5d", num);
		String binaryFormat = String.format("%32s", Long.toBinaryString(num)).replace(' ', '0');
		int bitCount = Long.bitCount(num);
		String remain = String.format("'%d' zeros, '%d' ones.", 64 - bitCount, bitCount);
		System.out.println(digitFormat + " : " + binaryFormat + ", " + remain);
	}

	public static int len(long num) {
		return (int) (Math.log10(num) + 1);
	}

	public static String nCopies(String string, int n) {
		if (n == 1) return string;
		String half = nCopies(string, n / 2);
		String full = half.concat(half);
		return n % 2 == 0 ? full : string.concat(full);
	}

	public static class Buffer<T> {
		public static final int DEFAULT_CAPACITY = 10;
		private final Queue<T> queue;
		private final int capacity;

		private final Object lock = new Object();

		public Buffer() {
			this(DEFAULT_CAPACITY);
		}

		public Buffer(int capacity) {
			queue = new ArrayDeque<>();
			this.capacity = capacity;
		}

		public void add(T element) {
			synchronized (lock) {
				while (queue.size() == capacity) {
					System.out.println("Queue is full, waiting...");
					tryCatch(() -> lock.wait());
					System.out.println("Let's check if queue is not full");
				}
				queue.add(element);
				lock.notify();
			}
		}

		public T get() {
			synchronized (lock) {
				while (queue.isEmpty()) {
					System.out.println("Queue is empty, waiting...");
					tryCatch(() -> lock.wait());
					System.out.println("Let's check if queue is not empty");
				}
				T element = queue.poll();
				lock.notifyAll();
				return element;
			}
		}
	}
}

