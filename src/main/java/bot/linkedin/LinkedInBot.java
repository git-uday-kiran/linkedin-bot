package bot.linkedin;

import bot.enums.EasyApplyOption;
import bot.linkedin.filters.JobApplyConfig;
import bot.linkedin.filters.JobSearchFilter;
import bot.linkedin.services.JobApplyFilterService;
import bot.linkedin.services.Sounds;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LinkedInBot extends BasePage implements ApplicationRunner {

	private final EasyJobApplier applier;
	private final JobSearchFilter searchFilter;
	private final JobApplyConfig applyConfig;
	private final JobApplyFilterService jobApplyFilterService;
	private final Sounds sounds;

	public LinkedInBot(WebDriver driver, EasyJobApplier applier, JobSearchFilter searchFilter, JobApplyConfig applyConfig, JobApplyFilterService jobApplyFilterService, Sounds sounds) {
		super(driver);
		this.applier = applier;
		this.searchFilter = searchFilter;
		this.applyConfig = applyConfig;
		this.jobApplyFilterService = jobApplyFilterService;
		this.sounds = sounds;
	}

	@Override
	public void run(ApplicationArguments args) {
		applier.goToEachUrlStartScanningAndApplyEasyApplyJobs(applyConfig.getJobsUrls());
		applier.goToEachUrlAndApplyEasyApplyJob(applyConfig.getEasyApplyJobUrls());

		if (searchFilter.getEasyApply() == EasyApplyOption.ENABLE) {
			applier.apply(searchFilter);
		}
		if (applyConfig.isApplyWithoutSearchFilter()) {
			applier.applyWithoutSearchFilter(searchFilter.getSearchQuery());
		}
		if (applyConfig.isScanJobsInHomePage()) {
			applier.applyJobsInHomePage();
		}

		log.info("Re-running the application");
		run(args);
	}

}
