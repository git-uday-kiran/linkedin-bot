package bot.linkedin;

import org.openqa.selenium.By;

public interface Locations {

	By EASY_APPLY = By.xpath("//span[text()='Easy Apply']");
	By NEXT = By.xpath("//span[text()='Next']");
	By REVIEW = By.xpath("//span[text()='Review']");
	By SUBMIT_APPLICATION = By.xpath("//span[text()='Submit application']");
	By CLOSE_WIDGET = By.xpath("//button[@aria-label='Dismiss']");
	By CONTINUE_APPLYING = By.xpath("//span[text()='Continue applying']");
	By JOB_CARDS_LOCATION = By.cssSelector("[data-view-name=\"job-card\"]");
	By EASY_APPLY_MODEL = By.cssSelector(".artdeco-modal-overlay--layer-default");
	By JOB_DESCRIPTION = By.id("job-details");

}
