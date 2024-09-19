package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum SortBy {

	MOST_RECENT(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[2]/fieldset/div/ul/li[1]/label/p/span[1]")),
	MOST_RELEVANT(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[2]/fieldset/div/ul/li[2]/label/p/span[1]"));

	final By location;

	SortBy(By location) {
		this.location = location;
	}
}
