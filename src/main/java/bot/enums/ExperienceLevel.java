package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum ExperienceLevel {

	INTERNSHIP(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[4]/fieldset/div/ul/li[1]/label/p/span[1]")),
	ENTRY_LEVEL(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[4]/fieldset/div/ul/li[2]/label/p/span[1]")),
	ASSOCIATE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[4]/fieldset/div/ul/li[3]/label/p/span[1]")),
	MID_SENIOR_LEVEL(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[4]/fieldset/div/ul/li[4]/label/p/span[1]")),
	DIRECTOR(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[4]/fieldset/div/ul/li[5]/label/p/span[1]")),
	EXECUTIVE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[4]/fieldset/div/ul/li[6]/label/p/span[1]"));

	final By location;

	ExperienceLevel(By location) {
		this.location = location;
	}
}
