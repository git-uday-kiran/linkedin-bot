package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum InYourNetwork {

	ENABLE(By.xpath("//h3[text()='In your network']/following-sibling::div/div")),
	DISABLE(By.xpath("//h3[text()='In your network']/following-sibling::div/div"));

	private final By location;

	InYourNetwork(By location) {
		this.location = location;
	}

}
