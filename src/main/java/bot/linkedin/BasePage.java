package bot.linkedin;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static bot.utils.Utils.tryCatchGet;

@Setter
@Getter
@Log4j2
public class BasePage {

	public final WebDriver driver;
	private WebDriverWait wait;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public WebElement find(By locator) {
		return driver.findElement(locator);
	}

	public List<WebElement> findAllWait(By locator) {
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public WebElement findWait(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
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
		WebElement element = find(locator);
		element.clear();
		element.sendKeys(keys);
	}

	public void click(By... locators) {
		for (By locator : locators) {
			WebElement element = find(locator);
			scrollJS(element);
			click(element);
		}
	}

	public void clickWait(By... locators) {
		for (By locator : locators) {
			WebElement element = findWait(locator);
			scrollJS(element);
			click(element);
		}
	}

	public void click(WebElement... elements) {
		for (WebElement element : elements) {
			Object clicked = element.getText().replace('\n', ' ');
			element.click();
			log.info("Clicked: {}", clicked);
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

	public void executeJS(String script, Object... args) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script, args);
	}

}
