package bot.linkedin;

import org.springframework.stereotype.Service;

@Service
public class SignalGiver {

	private final Object lock = new Object();

	public void waitForSignal() {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public void signal() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
}
