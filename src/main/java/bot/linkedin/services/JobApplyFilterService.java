package bot.linkedin.services;

import bot.linkedin.JobCard;
import bot.linkedin.filters.JobsApplyFilter;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplyFilterService {
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JobApplyFilterService.class);
    private final JobsApplyFilter applyFilter;

    private final ExperienceChecker experienceChecker;
    private final RegexUtils regexUtils;

    public JobApplyFilterService(JobsApplyFilter applyFilter, ExperienceChecker experienceChecker, RegexUtils regexUtils) {
        this.applyFilter = applyFilter;
        this.experienceChecker = experienceChecker;
        this.regexUtils = regexUtils;
    }

    public boolean canProcess(JobCard job) {
        log.info("Filtering job: {}", job);
        boolean canProcess = checkJobTitle(job.getTitle())
                && checkJobDescription(job.getDescription())
                && checkExperienceLevel(job);
        logProcess(canProcess);
        return canProcess;
    }

    private boolean checkExperienceLevel(JobCard job) {
        return experienceChecker.checkExperience('\n', job.getTitle(), job.getDescription());
    }

    public boolean checkJobTitle(String jobTitle) {
        JobsApplyFilter.JobTitleFilter jobTitleConfig = applyFilter.getJobTitle();

        boolean allMandatoryWordsExits = regexUtils.containsWords(jobTitle, jobTitleConfig.getMandatoryWords());
        log.info("Job title, all mandatory words found: {}", allMandatoryWordsExits);

        List<String> excludeWords = jobTitleConfig.getExcludeWords().stream()
                .filter(word -> regexUtils.containsWord(jobTitle, word))
                .toList();
        log.info("Job title, exclude words found: {}", excludeWords);

        List<String> includeWords = jobTitleConfig.getIncludeWords().stream()
                .filter(jobTitle::contains)
                .toList();
        log.info("Job title, include words found: {}", includeWords);

        return !includeWords.isEmpty() && excludeWords.isEmpty() && allMandatoryWordsExits;
    }

    public boolean checkJobDescription(String jobDescription) {
        return jobDescAllMandatoryWordsExist(jobDescription) && jobDescExcludeWordsNotFound(jobDescription);
    }

    private boolean jobDescAllMandatoryWordsExist(String jobDescription) {
        boolean allMandatoryWordsExit = regexUtils.containsWords(jobDescription, applyFilter.getJobDesc().getMandatoryWords());
        log.info("Job description, all mandatory words found: {}", allMandatoryWordsExit);
        return allMandatoryWordsExit;
    }

    private boolean jobDescExcludeWordsNotFound(String jobDescription) {
        var excludeWordsFound = applyFilter.getJobDesc()
                .getExcludeWords()
                .stream()
                .filter(jobDescription::contains).toList();
        log.info("Job description, exclude words found: {} ", excludeWordsFound);
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
