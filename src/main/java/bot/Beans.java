package bot;

import bot.enums.*;
import bot.linkedin.JobFilter;
import bot.linkedin.Under10Applicants;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.ProfilesIni;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Log4j2
@Configuration
public class Beans {

	@Bean
	WebDriver webDriver() {
		var profile = (new ProfilesIni()).getProfile("Default User");
		var options = new FirefoxOptions();
		options.setProfile(profile);
		log.info("Loading driver...");
		var firefoxDriver = new FirefoxDriver(options);
		log.info("Driver loaded");
		return firefoxDriver;
	}

	@Bean
	JobFilter jobFilter() {
		return JobFilter.builder()
			.sortBy(SortBy.MOST_RELEVANT)
			.datePosted(DatePosted.PAST_24_HOURS)
			.experienceLevels(List.of(ExperienceLevel.ENTRY_LEVEL, ExperienceLevel.ASSOCIATE))
			.jobTypes(List.of(JobType.FULL_TIME))
			.remotes(List.of(Remote.ONSITE, Remote.REMOTE, Remote.HYBRID))
			.easyApply(EasyApply.ENABLE)
			.under10Applicants(Under10Applicants.ENABLE)
			.build();
	}
}
