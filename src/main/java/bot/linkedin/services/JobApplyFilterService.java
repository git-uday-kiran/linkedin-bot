package bot.linkedin.services;

import bot.linkedin.filters.JobsApplyFilter;
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
		List<String> includeWords = applyFilter.getJobTitle().getIncludeWords().stream()
				.filter(jobTitle::contains).toList();
		log.info("Job title include words found: {}", includeWords);

		List<String> excludeWords = applyFilter.getJobTitle().getExcludeWords().stream()
				.filter(jobTitle::contains).toList();
		log.info("Job title exclude words found: {}", excludeWords);

		return !includeWords.isEmpty() && excludeWords.isEmpty();
	}

	private boolean checkJobDescription(String jobDescription) {
		return excludeWordsNotFound(jobDescription);
	}

	private boolean excludeWordsNotFound(String jobDescription) {
		var excludeWordsFound = applyFilter.getJobDesc().getExcludeWords().stream()
				.filter(jobDescription::contains).toList();
		log.info("Job description exclude words found: {} ", excludeWordsFound);
		return excludeWordsFound.isEmpty();
	}

	private void logProcess(boolean status, String jobTitle, String jobDescription) {
		if (status) {
			log.info("Job is suitable: job title: {{}}, job desc: {{}}", jobTitle, jobDescription);
		} else {
			log.warn("Job is not suitable: job title: {{}}, job desc: {{}}", jobTitle, jobDescription);
		}
	}

}
