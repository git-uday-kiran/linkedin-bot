package bot.linkedin;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum Under10Applicants {

	ENABLE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[13]/fieldset/div/div")),
	DISABLE(By.xpath("/html/body/div[3]/div/div/div[2]/ul/li[13]/fieldset/div/div"));

	final By location;

	Under10Applicants(By location) {
		this.location = location;
	}

}
