package bot.linkedin;

import bot.enums.EasyApplyOption;
import bot.linkedin.filters.JobSearchFilter;
import bot.linkedin.filters.JobsApplyFilter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static bot.utils.ThroatUtils.throatMedium;

@Log4j2
@Component
public class LinkedInBot extends BasePage implements ApplicationRunner {

	private final EasyJobApplier applier;
	private final JobsApplyFilter applyFilter;
	private final JobSearchFilter searchFilter;

	public LinkedInBot(WebDriver driver, EasyJobApplier applier, JobsApplyFilter applyFilter, JobSearchFilter searchFilter) {
		super(driver);
		this.applier = applier;
		this.applyFilter = applyFilter;
		this.searchFilter = searchFilter;
	}

	@Override
	public void run(ApplicationArguments args) {
		throatMedium();
		if (searchFilter.getEasyApply() == EasyApplyOption.ENABLE) {
			applier.apply(searchFilter);
		}
		if (applyFilter.isScanJobsInHomePage()) {
			applier.applyJobsInHomePage();
		}
		if (applyFilter.isApplyWithoutSearchFilter()) {
			applier.applyWithoutSearchFilter(searchFilter.getSearchQuery());
		}
	}

}
