package bot.linkedin;

import bot.enums.*;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import static bot.utils.ThroatUtils.*;

@Log4j2
@Component
public class Tasks extends BasePage {

	private final String email = "udaykiran0486@gmail.com";
	private final String password = "Uday.varma@0486";

	public Tasks(WebDriver driver) {
		super(driver);
	}

	public void goToHomePage() {
		driver.get("https://www.linkedin.com/");
	}

	public void login() {
		log.info("Signing in....");
		driver.findElement(By.linkText("Sign in")).click();
		WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));
		username.sendKeys(email);
		password.sendKeys(this.password);
		WebElement signIn = driver.findElement(By.xpath("/html/body/div/main/div[2]/div[1]/form/div[3]/button"));
		signIn.click();
		log.info("Successfully logged in :)");
	}

	public void clickJobs() {
		log.info("Clicking jobs...");
		By jobsLocator = By.linkText("Jobs");
		click(jobsLocator);
		throatLow();
	}

	public void performSearchQuery(String searchQuery) {
		log.info("Performing search query: {}", searchQuery);
		By searchLocator = By.xpath("/html/body/div[5]/header/div/div/div/div[2]/div[2]/div/div/input[1]");
		clickJS(searchLocator);
		set(searchLocator, searchQuery, Keys.ENTER);
		throatLow();
	}

	public void clickAdvancedFilters() {
		throatMedium();
		log.info("Clicking advanced filters option...");
		var location = By.xpath("/html/body/div[5]/div[3]/div[4]/section/div/section/div/div/div/div/div/button[1]");
		find(location).click();
		throatLow();
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

	public void applyFilter(JobFilter filter) {
		clickAdvancedFilters();
		throatLow();
		click(filter.getSortBy().getLocation());
		click(filter.getDatePosted().getLocation());
		filter.getExperienceLevels().forEach(exp -> click(exp.getLocation()));
		filter.getJobTypes().forEach(jt -> click(jt.getLocation()));
		filter.getRemotes().forEach(rt -> click(rt.getLocation()));
		if (filter.getEasyApply() == EasyApply.ENABLE) {
			click(filter.getEasyApply().getLocation());
		}
		if (filter.getUnder10Applicants() == Under10Applicants.ENABLE) {
			click(filter.getUnder10Applicants().getLocation());
		}
		By apply = By.xpath("/html/body/div[3]/div/div/div[3]/div/button[2]/span");
		log.info("Applying filter...");
		click(apply);
		throatLow();
	}
}
