package bot.utils;

import static bot.utils.Utils.sleep;

public final class ThroatUtils {

	private ThroatUtils() {
	}

	public static void throatLow() {
		throat(1);
	}

	public static void throatMedium() {
		throat(5);
	}

	public static void throatHigh() {
		throat(10);
	}

	public static void throatVeryHigh() {
		throat(30);
	}

	public static void throat(int seconds) {
		sleep(seconds);
	}
}
