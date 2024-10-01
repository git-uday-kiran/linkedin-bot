package bot.linkedin.services;

import bot.linkedin.filters.JobsApplyFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class JobApplyFilterService {

	private final JobsApplyFilter applyFilter;

	public boolean canProcess(String jobTitle, String jobDescription) {
		String jobTitleLowerCase = jobTitle.toLowerCase();
		String jobDescriptionLowerCase = jobDescription.toLowerCase();
		return process(jobTitleLowerCase, jobDescriptionLowerCase);
	}

	private boolean process(String jobTitle, String jobDescription) {
		log.info("Filtering job: Job Title: {{}}, Job Desc: {{}}", jobTitle, jobDescription);
		boolean canProcess = checkJobTitle(jobTitle) && checkJobDescription(jobDescription);
		logProcess(canProcess);
		return canProcess;
	}

	public boolean checkJobTitle(String jobTitle) {
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

	private void logProcess(boolean status) {
		if (status) {
			log.info("Job is suitable");
		} else {
			log.warn("Job is not suitable");
		}
	}

}
