package bot.linkedin;

import org.springframework.stereotype.Service;

@Service
public class SignalManager {

	public void waitForSignal(Object event) {
		synchronized (event) {
			try {
				event.wait();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public void signal(Object event) {
		synchronized (event) {
			event.notifyAll();
		}
	}


	enum Signals {
		SUBMIT;
	}
}
