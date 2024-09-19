package bot.utils;

@FunctionalInterface
public interface ThrowableRunnable {
	void run() throws Exception;
}
