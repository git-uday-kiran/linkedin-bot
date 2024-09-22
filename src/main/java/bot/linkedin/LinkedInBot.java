package bot.linkedin;

import bot.linkedin.services.JobSearchFilter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static bot.utils.ThroatUtils.throatMedium;
import static bot.utils.Utils.sleep;

@Log4j2
@Component
public class LinkedInBot extends BasePage implements ApplicationRunner {

	private final JobSearchFilter filter;
	private final EasyJobApplier applier;
	private final Tasks tasks;

	public LinkedInBot(WebDriver driver, JobSearchFilter filter, EasyJobApplier applier, Tasks tasks) {
		super(driver);
		this.filter = filter;
		this.applier = applier;
		this.tasks = tasks;
	}

	@Override
	public void run(ApplicationArguments args) {
		throatMedium();
		applier.apply();
		sleep(100000);
	}

	public void test() {
		scrollDown(200);
		By showAll = By.linkText("Show all");
		driver.findElement(showAll).click();
	}

}
