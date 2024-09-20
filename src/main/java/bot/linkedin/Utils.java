package bot.linkedin;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

@Log4j2
@Service
public class Utils extends BasePage {

	private final Scheduler scheduler;

	public Utils(WebDriver driver, Scheduler scheduler) {
		super(driver);
		this.scheduler = scheduler;
	}

	public void continuesClicker(By location, Duration period, String message) {
		continuesClicker(location, period, message, null);
	}

	public void continuesClicker(By location, Duration period, String message, Runnable action) {
		continuesChecker(location, period, element -> {
			log.info(message);
			element.click();
			if (action != null) action.run();
		});
	}

	public void continuesChecker(By location, Duration period, Consumer<WebElement> action) {
		Runnable task = () -> {
			try {
				List<WebElement> elements = driver.findElements(location);
				elements.forEach(element -> {
					if (element.isDisplayed() && element.isEnabled()) {
						if (action != null) action.accept(element);
					}
				});
			} catch (Exception ignored) {
			}
		};
		scheduler.schedule(task, period);
	}
}
