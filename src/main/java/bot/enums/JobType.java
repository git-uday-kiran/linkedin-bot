package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum JobType {

	FULL_TIME(By.xpath("//h3[text()='Job type']/following-sibling::div//span[text()='Full-time']")),
	PART_TIME(By.xpath("//h3[text()='Job type']/following-sibling::div//span[text()='Part-time']")),
	CONTRACT(By.xpath("//h3[text()='Job type']/following-sibling::div//span[text()='Contract']")),
	INTERNSHIP(By.xpath("//h3[text()='Job type']/following-sibling::div//span[text()='Internship']")),
	OTHER(By.xpath("//h3[text()='Job type']/following-sibling::div//span[text()='Other']"));

	final By location;

	JobType(By location) {
		this.location = location;
	}
}
