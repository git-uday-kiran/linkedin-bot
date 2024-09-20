package bot.linkedin;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Optional;

import static bot.utils.Utils.*;

@Setter
@Getter
@Log4j2
@AllArgsConstructor
public class BasePage {

	public WebDriver driver;

	public WebElement find(By locator) {
		return driver.findElement(locator);
	}

	public Optional<WebElement> findOptional(By locator) {
		return tryCatchGet(() -> find(locator));
	}

	public Optional<WebElement> findClickableOptional(By location) {
		Optional<WebElement> optional = findOptional(location);
		if (optional.isPresent()) {
			WebElement element = optional.get();
			if (element.isDisplayed() && element.isEnabled()) {
				return Optional.of(element);
			}
		}
		return Optional.empty();
	}

	@PostConstruct
	public void postConstruct() {
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
			log.info("Clicked: {}", locator);
		}
	}

	public void clickJS(By... locators) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (By locator : locators) {
			js.executeScript("arguments[0].click()", find(locator));
		}
	}

	public void scrollJS(By locator) {
		scrollJS(find(locator));
	}

	public void scrollJS(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}

	public void scrollDown(double percentage) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0," + (5 * percentage) + ")", "");
	}

	public void scrollDown() {
		scrollDown(100);
	}

	public void scrollBottomOfPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void clickJS(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	public void executeJS(String script, By locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script, find(locator));
	}

}
