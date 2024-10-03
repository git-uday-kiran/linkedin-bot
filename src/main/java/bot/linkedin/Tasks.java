package bot.linkedin;

import bot.enums.*;
import bot.linkedin.filters.JobSearchFilter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static bot.utils.ThroatUtils.throatLow;
import static bot.utils.ThroatUtils.throatMedium;

@Log4j2
@Component
public class Tasks extends BasePageV1 {

	private final String email = "udaykiran0486@gmail.com";
	private final String password = "xxxxxx";

	public Tasks(WebDriver driver) {
		super(driver);
	}

	public void goToHomePage() {
		driver.get("https://www.linkedin.com/");
	}

	public void login() {
		log.info("Signing in....");
		Optional<WebElement> op = tryFindElement(By.linkText("Sign in"));
		if (op.isPresent()) {
			WebElement username = driver.findElement(By.id("username"));
			WebElement password = driver.findElement(By.id("password"));
			username.sendKeys(email);
			password.sendKeys(this.password);
			WebElement signIn = driver.findElement(By.xpath("/html/body/div/main/div[2]/div[1]/form/div[3]/button"));
			signIn.click();
			log.info("Successfully logged in :)");
		} else {
			log.warn("Sign in button not found");
		}
	}

	public void clickJobs() {
		log.info("Clicking jobs...");
		By jobsLocator = By.linkText("Jobs");
		WebElement jobsOption = waitForElementPresence(jobsLocator);
		waitForClickable(jobsOption);
		click(jobsOption);
	}

	public void performSearchQuery(String searchQuery) {
		log.info("Performing search query: {}", searchQuery);
		By searchLocator = By.xpath("//div[contains(@class,'jobs-search-box__input')][1]//input[1]");
		WebElement searchBox = waitForElementPresence(searchLocator);
		waitForClickable(searchBox);
		click(searchBox);
		searchBox.sendKeys(searchQuery, Keys.ENTER);
	}

	public void clickAdvancedFilters() {
		throatMedium();
		log.info("Clicking advanced filters option...");
		var location = By.xpath("//button[text()='All filters']");
		WebElement filtersOption = waitForElementPresence(location);
		waitForClickable(filtersOption);
		click(filtersOption);
		throatLow();
	}

	public void applyFilter(JobSearchFilter filter) {
		clickAdvancedFilters();
		throatLow();

		findAndClick(filter.getSortBy().getLocation());
		findAndClick(filter.getDatePosted().getLocation());

		filter.getExperienceLevels().stream()
				.map(ExperienceLevel::getLocation)
				.map(this::findElement)
				.forEach(this::click);

		filter.getJobTypes().stream()
				.map(JobType::getLocation)
				.map(this::findElement)
				.forEach(this::click);

		filter.getRemotes().stream()
				.map(Remote::getLocation)
				.map(this::findElement)
				.forEach(this::click);

		if (filter.getEasyApply() == EasyApplyOption.ENABLE) {
			findAndClick(filter.getEasyApply().getLocation());
		}

		filter.getLocations().stream()
				.map(Location::getLocation)
				.map(this::tryFindElement)
				.flatMap(Optional::stream)
				.forEach(this::click);

		if (filter.getUnder10Applicants() == Under10Applicants.ENABLE) {
			findAndClick(filter.getUnder10Applicants().getLocation());
		}

		By applyFilterLocation = By.xpath("/html/body/div[4]/div/div/div[3]/div/button[2]/span");
		WebElement applyFilter = waitForElementPresence(applyFilterLocation);
		log.info("Applying filter...");
		click(applyFilter);
		throatLow();
	}

}
