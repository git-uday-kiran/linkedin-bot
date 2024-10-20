package bot.linkedin;

import org.openqa.selenium.By;

public interface Locations {

	By EASY_APPLY = By.xpath("//button[.//span[text()='Easy Apply']]");
	By NEXT = By.xpath("//span[text()='Next']");
	By REVIEW = By.xpath("//span[text()='Review']");
	By SUBMIT_APPLICATION = By.xpath("//span[text()='Submit application']");
	By CLOSE_WIDGET = By.xpath("//button[@data-test-modal-close-btn]");
	By CONTINUE_APPLYING = By.xpath("//span[text()='Continue applying']");
	By JOB_CARDS_LOCATION = By.xpath("//div[contains(@class,'jobs-search-results-list')]/ul[@class='scaffold-layout__list-container']/li");
	By EASY_APPLY_MODEL = By.xpath("//div[@role='dialog']");
	By JOB_DESCRIPTION = By.id("job-details");
	By SHOW_ALL_LOCATION = By.cssSelector(".discovery-templates-vertical-list__footer > a");
	By CLOSE_BUTTON = By.xpath("//span[text()='Done']");

}
