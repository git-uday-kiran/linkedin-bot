package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum DatePosted {

	ANY_TIME(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[3]/fieldset/div/ul/li[1]/label/p/span[1]")),
	PAST_MONTH(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[3]/fieldset/div/ul/li[2]/label/p/span[1]")),
	PAST_WEEK(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[3]/fieldset/div/ul/li[3]/label/p/span[1]")),
	PAST_24_HOURS(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[3]/fieldset/div/ul/li[4]/label/p/span[1]"));

	final By location;

	DatePosted(By location) {
		this.location = location;
	}
}
