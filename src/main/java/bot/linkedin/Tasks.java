package bot.linkedin;

import bot.enums.EasyApplyOption;
import bot.enums.Under10Applicants;
import bot.linkedin.filters.JobSearchFilter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static bot.utils.ThroatUtils.*;

@Log4j2
@Component
public class Tasks extends BasePage {

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
		Optional<WebElement> op = findOptional(By.linkText("Sign in"));
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
		click(jobsLocator);
		throatLow();
	}

	public void performSearchQuery(String searchQuery) {
		log.info("Performing search query: {}", searchQuery);
		By searchLocator = By.xpath("//div[contains(@class,'jobs-search-box__input')][1]//input[1]");
		throatMedium();
		click(searchLocator);
		set(searchLocator, searchQuery, Keys.ENTER);
		throatMedium();
	}

	public void clickAdvancedFilters() {
		throatMedium();
		log.info("Clicking advanced filters option...");
		var location = By.xpath("//button[text()='All filters']");
		find(location).click();
		throatLow();
	}

	public void applyFilter(JobSearchFilter filter) {
		clickAdvancedFilters();
		throatLow();
		click(filter.getSortBy().getLocation());
		click(filter.getDatePosted().getLocation());
		filter.getExperienceLevels().forEach(exp -> click(exp.getLocation()));
		filter.getJobTypes().forEach(jt -> click(jt.getLocation()));
		filter.getRemotes().forEach(rt -> click(rt.getLocation()));
		if (filter.getEasyApply() == EasyApplyOption.ENABLE) {
			click(filter.getEasyApply().getLocation());
		}
		filter.getLocations().forEach(lc -> tryClick(lc.getLocation()));
		if (filter.getUnder10Applicants() == Under10Applicants.ENABLE) {
			click(filter.getUnder10Applicants().getLocation());
		}
		By apply = By.xpath("/html/body/div[4]/div/div/div[3]/div/button[2]/span");
		throatMedium();
		log.info("Applying filter...");
		click(apply);
		throatLow();
	}
}
