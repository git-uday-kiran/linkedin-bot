package bot.linkedin;

import org.openqa.selenium.By;

public interface Locations {

	By EASY_APPLY = By.xpath("//span[text()='Easy Apply']");
	By NEXT = By.xpath("//span[text()='Next']");
	By REVIEW = By.xpath("//span[text()='Review']");
	By SUBMIT_APPLICATION = By.xpath("//span[text()='Submit application']");
	//	By CLOSE_WIDGET = By.xpath("//button/span[text()='Done']");
	By CLOSE_WIDGET = By.xpath("/html/body/div[3]/div/div/button");
	By CONTINUE_APPLYING = By.xpath("//span[text()='Continue applying']");
	By JOB_CARDS_LOCATION = new By.ByCssSelector("div.job-card-container");

}
