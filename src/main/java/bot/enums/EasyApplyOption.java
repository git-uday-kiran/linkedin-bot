package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum EasyApplyOption {

	ENABLE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[8]/fieldset/div/div")),
	DISABLE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[8]/fieldset/div/div"));

	final By location;

	EasyApplyOption(By location) {
		this.location = location;
	}
}

