package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum SortBy {

	MOST_RECENT(By.xpath("//span[text()='Most recent']")),
	MOST_RELEVANT(By.xpath("//span[text()='Most relevant']"));

	final By location;

	SortBy(By location) {
		this.location = location;
	}
}
