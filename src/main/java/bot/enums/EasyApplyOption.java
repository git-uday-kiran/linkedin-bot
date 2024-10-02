package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum EasyApplyOption {

	ENABLE(By.xpath("//h3[text()='Easy Apply']/following-sibling::div/div")),
	DISABLE(By.xpath("//h3[text()='Easy Apply']/following-sibling::div/div"));

	final By location;

	EasyApplyOption(By location) {
		this.location = location;
	}
}

