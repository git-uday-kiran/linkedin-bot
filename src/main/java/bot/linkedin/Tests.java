package bot.linkedin;

import bot.enums.*;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static bot.utils.ThroatUtils.throatLow;

@Log4j2
public class Tests extends BasePage {
	public Tests(WebDriver driver) {
		super(driver);
	}

	public void clickSortBy() {
		log.info("Clicking all sort by options...");
		for (var posted : SortBy.values()) {
			throatLow();
			click(posted.getLocation());
		}
	}

	public void clickDatePosted() {
		log.info("Clicking all data posted options...");
		for (var posted : DatePosted.values()) {
			throatLow();
			click(posted.getLocation());
		}
	}

	public void clickExperienceLevel() {
		log.info("Clicking all experience levels...");
		for (var level : ExperienceLevel.values()) {
			throatLow();
			click(level.getLocation());
		}
	}

	public void clickJobTypes() {
		log.info("Clicking all job types...");
		for (var jobType : JobType.values()) {
			throatLow();
			By location = jobType.getLocation();
			System.out.println("Clicking.... " + jobType.name());
			scrollJS(location);
			click(location);
		}
	}

	public void clickRemotes() {
		log.info("Clicking all remote options...");
		for (var level : Remote.values()) {
			throatLow();
			By location = level.getLocation();
			scrollJS(location);
			click(location);
		}
	}
}
