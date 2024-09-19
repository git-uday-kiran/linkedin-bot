package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum JobType {

	FULL_TIME(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[6]/fieldset/div/ul/li[1]/label/p/span[1]")),

	PART_TIME(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[6]/fieldset/div/ul/li[2]/label/p/span[1]")),

	CONTRACT(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[6]/fieldset/div/ul/li[3]/label/p/span[1]")),

	TEMPORARY(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[6]/fieldset/div/ul/li[4]/label/p/span[1]")),

	INTERNSHIP(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[6]/fieldset/div/ul/li[5]/label/p/span[1]")),

	OTHER(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[6]/fieldset/div/ul/li[6]/label/p/span[1]"));

	final By location;

	JobType(By location) {
		this.location = location;
	}
}
