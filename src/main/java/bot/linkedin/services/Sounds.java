package bot.linkedin.services;

import org.springframework.stereotype.Service;

import static io.vavr.control.Try.run;

@Service
public class Sounds {

	private static final String MUSIC_DIR = "/home/uday-kiran/Music/";

	public void finished() {
		playMusic("notification.mp3");
	}

	public void appliedJob() {
		playMusic("positive.mp3");
	}

	public void alert() {
		playMusic("alert.mp3");
	}

	public void playMusic(String fileName) {
		Runtime runtime = Runtime.getRuntime();
		String[] command = {"mplayer", MUSIC_DIR + fileName};
		run(() -> runtime.exec(command));
	}
}
