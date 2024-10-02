package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum Under10Applicants {

	ENABLE(By.xpath("//h3[text()='Under 10 applicants']/following-sibling::div/div")),
	DISABLE(By.xpath("//h3[text()='Under 10 applicants']/following-sibling::div/div"));

	final By location;

	Under10Applicants(By location) {
		this.location = location;
	}

}
