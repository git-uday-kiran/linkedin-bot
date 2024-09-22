package bot.linkedin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.stream;

@Log4j2
@Service
@RequiredArgsConstructor
public class JobApplyFilterService {

	private final JobsApplyFilter applyFilter;

	public boolean canProcess(String jobTitle, String jobDescription) {
		String jobTitleOneLine = jobTitle.replace('\n', ' ').toLowerCase();
		String jobDescriptionOneLine = jobDescription.replace('\n', ' ').toLowerCase();
		return process(jobTitleOneLine, jobDescriptionOneLine);
	}

	private boolean process(String jobTitle, String jobDescription) {
		log.info("Filtering => job title: {{}}, job desc: {{}}", jobTitle, jobDescription);
		boolean canProcess = checkJobTitle(jobTitle) && checkJobDescription(jobDescription);
		logProcess(canProcess, jobTitle, jobDescription);
		return canProcess;
	}

	private boolean checkJobTitle(String jobTitle) {
		List<String> foundTitleParts = applyFilter.getJobTitle().getIncludeWords().stream()
			.filter(jobTitle::contains)
			.toList();
		log.info("Found title parts: {}", foundTitleParts);
		return !foundTitleParts.isEmpty();
	}

	private boolean checkJobDescription(String jobDescription) {
		return excludeWordsNotFound(jobDescription);
	}

	private boolean excludeWordsNotFound(String jobDescription) {
		String[] words = jobDescription.split(" ");
		var excludeWordsFound = stream(words)
			.map(String::toLowerCase)
			.filter(applyFilter.getJobDesc().getExcludeWords()::contains)
			.toList();
		log.info("Exclude words found: {} ", excludeWordsFound);
		return excludeWordsFound.isEmpty();
	}

	private void logProcess(boolean status, String jobTitle, String jobDescription) {
		if (status) {
			log.info("Job description is suitable: job title: {{}}, job desc: {{}}", jobTitle, jobDescription);
		} else {
			log.warn("Job description is not suitable: job title: {{}}, job desc: {{}}", jobTitle, jobDescription);
		}
	}

}
