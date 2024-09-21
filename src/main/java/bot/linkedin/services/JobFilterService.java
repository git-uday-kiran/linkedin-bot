package bot.linkedin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static java.util.Arrays.stream;

@Log4j2
@Service
@RequiredArgsConstructor
public class JobFilterService {

	private final JobDescFilter descFilter;

	public boolean canProcess(String jobDescription) {
		log.info("Checking job description: {}", jobDescription);
		String[] words = jobDescription.split(" ");

		var excludeFound = stream(words).filter(descFilter.excludeWords::contains).toList();
		log.info("Exclude words found: {} ", excludeFound);
		boolean canProcess = excludeFound.isEmpty();

		logProcess(canProcess, jobDescription);
		return canProcess;
	}

	private void logProcess(boolean status, String jobDescription) {
		if (status) {
			log.info("Job description is suitable: {}", jobDescription);
		} else {
			log.warn("Job description is not suitable: {}", jobDescription);
		}
	}

}
