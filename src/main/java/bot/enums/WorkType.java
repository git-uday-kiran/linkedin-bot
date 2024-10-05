package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum WorkType {

	ONSITE(By.xpath("//h3[text()='Remote']/following-sibling::div//span[text()='On-site']")),
	HYBRID(By.xpath("//h3[text()='Remote']/following-sibling::div//span[text()='Hybrid']")),
	REMOTE(By.xpath("//h3[text()='Remote']/following-sibling::div//span[text()='Remote']"));

	final By location;

	WorkType(By location) {
		this.location = location;
	}
}
