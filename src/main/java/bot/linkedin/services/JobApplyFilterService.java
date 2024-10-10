package bot.linkedin.services;

import bot.linkedin.JobCard;
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
	private final YearMatcher yearMatcher;

	public boolean canProcess(JobCard job) {
		log.info("Filtering job: {}", job);
		boolean canProcess = checkJobTitle(job.getTitle())
				&& checkJobDescription(job.getDescription())
				&& checkExperienceLevel(job);
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

	public boolean checkJobDescription(String jobDescription) {
		return jobDescMandatoryWordsExist(jobDescription) && jobDescExcludeWordsNotFound(jobDescription);
	}

	private boolean checkExperienceLevel(JobCard job) {
		return yearMatcher.checkExperience(job.getLineSeparator(), job.getTitle(), job.getDescription());
	}

	private boolean jobDescExcludeWordsNotFound(String jobDescription) {
		var excludeWordsFound = applyFilter.getJobDesc()
				.getExcludeWords()
				.stream()
				.filter(jobDescription::contains).toList();
		log.info("Job description exclude words found: {} ", excludeWordsFound);
		return excludeWordsFound.isEmpty();
	}

	private boolean jobDescMandatoryWordsExist(String jobDescription) {
		boolean allExist = applyFilter.getJobDesc()
				.getMandatoryWords()
				.stream()
				.allMatch(jobDescription::contains);
		log.info("All job description mandatory words found: {}", allExist);
		return allExist;
	}

	private void logProcess(boolean status) {
		if (status) {
			log.info("Job is suitable");
		} else {
			log.warn("Job is not suitable");
		}
	}

}
