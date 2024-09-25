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
import java.util.Collections;
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

	public List<WebElement> findAllClickable(By locator) {
		return tryCatchGet(() -> driver.findElements(locator))
				.map(list -> list.stream()
						.filter(element -> element.isDisplayed() && element.isEnabled())
						.toList())
				.orElse(Collections.emptyList());
	}

	@PostConstruct
	public void postConstruct() {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofHours(1));
	}

	public void set(By locator, CharSequence... keys) {
		WebElement element = find(locator);
		element.clear();
		element.sendKeys(keys);
	}

	public void clickWait(By... locators) {
		for (By locator : locators) {
			clickWait(findWait(locator));
		}
	}

	public void clickWait(WebElement element) {
		WebElement target = wait.until(ExpectedConditions.elementToBeClickable(element));
		click(target);
	}

	public void click(By... locators) {
		for (By locator : locators) {
			click(find(locator));
		}
	}

	public void click(WebElement... elements) {
		for (WebElement element : elements) {
			Object clicked = element.getText().replace('\n', ' ');
			scrollJS(element);
			highlight(elements);
			element.click();
			log.info("Clicked: {}", clicked);
		}
	}

	public void clickJS(By... locators) {
		for (By locator : locators) {
			clickJS(find(locator));
		}
	}

	public void clickJS(WebElement... webElements) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (WebElement element : webElements) {
			scrollJS(element);
			highlight(webElements);
			js.executeScript("arguments[0].click();", element);
		}
	}

	public void highlight(WebElement... webElements) {
		String script = "arguments[0].setAttribute('style', 'border: 2px solid yellow')";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (WebElement webElement : webElements) {
			js.executeScript(script, webElement);
		}
	}


	public void scrollJS(By locator) {
		scrollJS(find(locator));
	}

	public void scrollJS(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}

	public void scrollDown(double pixels) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0," + pixels + ")", "");
	}

	public void scrollDown() {
		scrollDown(500);
	}

	public void scrollBottomOfPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}


}
