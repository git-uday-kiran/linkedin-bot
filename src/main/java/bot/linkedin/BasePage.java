package bot.linkedin;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Optional;

import static bot.utils.Utils.*;

@Setter
@Getter
@AllArgsConstructor
public class BasePage {

	public WebDriver driver;

	public WebElement find(By locator) {
		return driver.findElement(locator);
	}

	public Optional<WebElement> findOptional(By locator) {
		return tryCatchGet(() -> find(locator));
	}

	@PostConstruct
	public void init() {
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofHours(1));
	}

	public void set(By locator, CharSequence... keys) {
		find(locator).clear();
		find(locator).sendKeys(keys);
	}

	public void click(By... locators) {
		for (By locator : locators) {
			tryCatch(() -> scrollJS(locator));
			find(locator).click();
		}
	}

	public void clickJS(By... locators) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (By locator : locators) {
			js.executeScript("arguments[0].click()", find(locator));
		}
	}

	public void scrollJS(By locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", find(locator));
	}

	public void scrollDown(double percentage) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0," + (5 * percentage) + ")", "");
	}

	public void scrollDown() {
		scrollDown(100);
	}

	public void executeJS(String script, By locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script, find(locator));
	}

}
