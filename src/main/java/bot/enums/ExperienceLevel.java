package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum ExperienceLevel {

	INTERNSHIP(By.xpath("//h3[text()='Experience level']/following-sibling::div//span[text()='Internship']")),
	ENTRY_LEVEL(By.xpath("//h3[text()='Experience level']/following-sibling::div//span[text()='Entry level']")),
	ASSOCIATE(By.xpath("//h3[text()='Experience level']/following-sibling::div//span[text()='Associate']")),
	MID_SENIOR_LEVEL(By.xpath("//h3[text()='Experience level']/following-sibling::div//span[text()='Mid-Senior level']")),
	DIRECTOR(By.xpath("//h3[text()='Experience level']/following-sibling::div//span[text()='Director']")),
	EXECUTIVE(By.xpath("//h3[text()='Experience level']/following-sibling::div//span[text()='Executive']"));

	final By location;

	ExperienceLevel(By location) {
		this.location = location;
	}
}
