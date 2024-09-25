package bot.utils;

import java.util.concurrent.TimeUnit;

import static io.vavr.control.Try.run;

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
		run(() -> TimeUnit.SECONDS.sleep(seconds));
	}
}
