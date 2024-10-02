package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum DatePosted {

	ANY_TIME(By.xpath("//span[text()='Any time']")),
	PAST_MONTH(By.xpath("//span[text()='Past month']")),
	PAST_WEEK(By.xpath("//span[text()='Past week']")),
	PAST_24_HOURS(By.xpath("//span[text()='Past 24 hours']"));

	final By location;

	DatePosted(By location) {
		this.location = location;
	}
}
