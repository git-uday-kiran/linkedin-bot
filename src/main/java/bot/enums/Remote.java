package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum Remote {

	ONSITE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[7]/fieldset/div/ul/li[1]/label/p/span[1]")),
	HYBRID(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[7]/fieldset/div/ul/li[2]/label/p/span[1]")),
	REMOTE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[7]/fieldset/div/ul/li[3]/label/p/span[1]"));

	final By location;

	Remote(By location) {
		this.location = location;
	}
}
