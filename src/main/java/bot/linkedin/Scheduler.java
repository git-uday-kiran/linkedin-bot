package bot.linkedin;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class Scheduler {
	private final Timer timer = new Timer();

	public void schedule(Runnable task, Duration period) {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				task.run();
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, period.toMillis());
	}
}
